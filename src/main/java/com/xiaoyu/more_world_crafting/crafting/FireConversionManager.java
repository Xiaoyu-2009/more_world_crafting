package com.xiaoyu.more_world_crafting.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.xiaoyu.more_world_crafting.recipe.FireConversionRecipe;
import com.xiaoyu.more_world_crafting.recipe.ModRecipeTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class FireConversionManager {
    private static final Map<UUID, ConversionData> pendingConversions = new HashMap<>();
    private static final Map<ResourceLocation, FireConversionRecipe> customRecipes = new HashMap<>();
    private static final double FIRE_JUMP_HEIGHT = 0.8;
    private static final int CONVERSION_DELAY = 10;

    public static void tick(Level level) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        for (var entity : serverLevel.getAllEntities()) {
            if (!(entity instanceof ItemEntity item)) continue;
            if (item.isRemoved() || item.getItem().isEmpty()) continue;
            
            FireConversionRecipe recipe = findMatchingRecipe(serverLevel, item.getItem());
            if (recipe != null && isCorrectFireType(serverLevel, item.position(), recipe.getFireType())) {
                UUID itemId = item.getUUID();
                if (!pendingConversions.containsKey(itemId)) {
                    Player targetPlayer = getItemThrower(item);
                    pendingConversions.put(itemId, new ConversionData(itemId, targetPlayer, recipe, item.getItem().copy(), item.position()));
                }
            }
        }

        processConversions(serverLevel);
        processJumpingOutputItems(serverLevel);
    }

    private static void processConversions(ServerLevel level) {
        Iterator<Map.Entry<UUID, ConversionData>> iterator = pendingConversions.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<UUID, ConversionData> entry = iterator.next();
            ConversionData data = entry.getValue();
            
            data.tickCount++;
            
            if (data.tickCount >= CONVERSION_DELAY) {
                ItemEntity targetItem = null;
                for (var entity : level.getAllEntities()) {
                    if (entity instanceof ItemEntity item && item.getUUID().equals(data.itemId) && !item.isRemoved()) {
                        targetItem = item;
                        break;
                    }
                }
                
                if (targetItem != null) {
                    if (isCorrectFireType(level, targetItem.position(), data.recipe.getFireType())) {
                        boolean conversionSuccess = performConversion(level, data.originalItemStack, data.recipe, data.targetPlayer, data.position);
                        if (conversionSuccess) {
                            targetItem.discard();
                        }
                    }
                } else {
                    performConversion(level, data.originalItemStack, data.recipe, data.targetPlayer, data.position);
                }

                iterator.remove();
            }
        }
    }

    private static Player getItemThrower(ItemEntity item) {
        if (item.getOwner() instanceof Player player) {
            return player;
        }
        return null;
    }

    private static FireConversionRecipe findMatchingRecipe(ServerLevel level, ItemStack itemStack) {
        List<FireConversionRecipe> allRecipes = new ArrayList<>();
        
        List<FireConversionRecipe> datapackRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.FIRE_CONVERSION.get());
        allRecipes.addAll(datapackRecipes);
        
        allRecipes.addAll(customRecipes.values());
        
        for (FireConversionRecipe recipe : allRecipes) {
            if (recipe.getIngredient().test(itemStack)) {
                return recipe;
            }
        }
        
        return null;
    }

    private static boolean performConversion(ServerLevel level, ItemStack originalStack, FireConversionRecipe recipe, Player targetPlayer, Vec3 position) {
        int inputCount = originalStack.getCount();
        
        if (inputCount <= 0) {
            return false;
        }

        int successfulConversions = CraftingAPI.calculateSuccessfulConversions(inputCount, recipe.getConversionChance(), level);
        
        if (successfulConversions <= 0) {
            return false;
        }
        
        ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
        if (result.isEmpty()) {
            return false;
        }

        int resultPerInput = result.getCount();
        int totalResultCount = Math.max(1, successfulConversions * resultPerInput);
        result.setCount(totalResultCount);

        ItemEntity resultEntity = CraftingAPI.createOutputItemWithJump(level, position, result, null, FIRE_JUMP_HEIGHT);
        
        return level.addFreshEntity(resultEntity);
    }

    private static boolean isCorrectFireType(ServerLevel level, Vec3 position, FireConversionRecipe.FireType fireType) {
        BlockPos pos = BlockPos.containing(position);
        BlockPos posBelow = pos.below();
        
        switch (fireType) {
            case NORMAL_FIRE:
                return level.getBlockState(pos).is(Blocks.FIRE) || 
                       level.getBlockState(posBelow).is(Blocks.FIRE);
            case SOUL_FIRE:
                return level.getBlockState(pos).is(Blocks.SOUL_FIRE) ||
                       level.getBlockState(posBelow).is(Blocks.SOUL_FIRE);
            case CAMPFIRE:
                return level.getBlockState(pos).is(Blocks.CAMPFIRE) ||
                       level.getBlockState(posBelow).is(Blocks.CAMPFIRE);
            case SOUL_CAMPFIRE:
                return level.getBlockState(pos).is(Blocks.SOUL_CAMPFIRE) ||
                       level.getBlockState(posBelow).is(Blocks.SOUL_CAMPFIRE);
            default:
                return false;
        }
    }

    private static void performFireJump(ItemEntity itemEntity) {
        Vec3 velocity = itemEntity.getDeltaMovement();
        Vec3 jumpVelocity = new Vec3(
            velocity.x,
            FIRE_JUMP_HEIGHT,
            velocity.z
        );
        
        itemEntity.setDeltaMovement(jumpVelocity);
        itemEntity.hasImpulse = true;
    }

    private static void processJumpingOutputItems(ServerLevel level) {
        for (var entity : level.getAllEntities()) {
            if (!(entity instanceof ItemEntity item)) continue;
            if (item.isRemoved() || !item.isInvulnerable()) continue;
            
            BlockPos pos = item.blockPosition();
            if (isInFireEnvironment(level, pos)) {
                performFireJump(item);
            }
        }
    }

    private static boolean isInFireEnvironment(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.FIRE) ||
               level.getBlockState(pos).is(Blocks.SOUL_FIRE) ||
               level.getBlockState(pos).is(Blocks.CAMPFIRE) ||
               level.getBlockState(pos).is(Blocks.SOUL_CAMPFIRE) ||
               level.getBlockState(pos.below()).is(Blocks.FIRE) ||
               level.getBlockState(pos.below()).is(Blocks.SOUL_FIRE) ||
               level.getBlockState(pos.below()).is(Blocks.CAMPFIRE) ||
               level.getBlockState(pos.below()).is(Blocks.SOUL_CAMPFIRE);
    }

    public static void addCustomRecipe(FireConversionRecipe recipe) {
        customRecipes.put(recipe.getId(), recipe);
    }
    
    public static void removeCustomRecipe(ResourceLocation recipeId) {
        customRecipes.remove(recipeId);
    }
    
    public static java.util.Collection<FireConversionRecipe> getCustomRecipes() {
        return customRecipes.values();
    }

    private static class ConversionData {
        UUID itemId;
        Player targetPlayer;
        FireConversionRecipe recipe;
        ItemStack originalItemStack;
        Vec3 position;
        int tickCount;
        
        ConversionData(UUID itemId, Player targetPlayer, FireConversionRecipe recipe, ItemStack originalItemStack, Vec3 position) {
            this.itemId = itemId;
            this.targetPlayer = targetPlayer;
            this.recipe = recipe;
            this.originalItemStack = originalItemStack;
            this.position = position;
            this.tickCount = 0;
        }
    }
}