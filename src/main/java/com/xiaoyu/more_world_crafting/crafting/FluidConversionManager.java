package com.xiaoyu.more_world_crafting.crafting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

import com.xiaoyu.more_world_crafting.recipe.FluidConversionRecipe;
import com.xiaoyu.more_world_crafting.recipe.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

public class FluidConversionManager {
    private static final Map<UUID, ConversionData> pendingConversions = new HashMap<>();
    private static final Map<ResourceLocation, FluidConversionRecipe> customRecipes = new HashMap<>();
    private static final double LAVA_JUMP_HEIGHT = 0.8;
    private static final int CONVERSION_DELAY = 10;

    public static void tick(Level level) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        for (var entity : serverLevel.getAllEntities()) {
            if (!(entity instanceof ItemEntity item)) continue;
            if (item.getItem().isEmpty()) continue;
            
            FluidConversionRecipe recipe = findMatchingRecipe(serverLevel, item.getItem());
            if (recipe != null && isInCorrectFluid(serverLevel, item.position(), recipe)) {
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
                performConversion(level, data.originalItemStack, data.recipe, data.targetPlayer, data.position);

                for (var entity : level.getAllEntities()) {
                    if (entity instanceof ItemEntity item && item.getUUID().equals(data.itemId)) {
                        item.discard();
                        break;
                    }
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

    private static FluidConversionRecipe findMatchingRecipe(ServerLevel level, ItemStack itemStack) {
        List<FluidConversionRecipe> allRecipes = new ArrayList<>();
        
        List<FluidConversionRecipe> datapackRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.FLUID_CONVERSION.get());
        allRecipes.addAll(datapackRecipes);
        
        allRecipes.addAll(customRecipes.values());
        
        for (FluidConversionRecipe recipe : allRecipes) {
            if (recipe.getIngredient().test(itemStack)) {
                return recipe;
            }
        }
        
        return null;
    }

    private static boolean performConversion(ServerLevel level, ItemStack originalStack, FluidConversionRecipe recipe, Player targetPlayer, Vec3 position) {
        int inputCount = originalStack.getCount();
        
        if (inputCount <= 0) {
            return false;
        }

        if (level.random.nextFloat() > recipe.getConversionChance()) {
            return false;
        }
        
        ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
        if (result.isEmpty()) {
            return false;
        }

        int resultPerInput = result.getCount();
        int totalResultCount = Math.max(1, inputCount * resultPerInput);
        result.setCount(totalResultCount);

        ItemEntity resultEntity = CraftingAPI.createOutputItemWithJump(level, position, result, null, LAVA_JUMP_HEIGHT);
        
        return level.addFreshEntity(resultEntity);
    }

    private static boolean isInCorrectFluid(ServerLevel level, Vec3 position, FluidConversionRecipe recipe) {
        BlockPos pos = BlockPos.containing(position);
        BlockPos posBelow = pos.below();
        
        return 
        level.getFluidState(pos).is(recipe.getRequiredFluid()) || 
        level.getFluidState(posBelow).is(recipe.getRequiredFluid());
    }

    private static void performLavaJump(ItemEntity itemEntity) {
        Vec3 velocity = itemEntity.getDeltaMovement();
        Vec3 jumpVelocity = new Vec3(
            velocity.x,
            LAVA_JUMP_HEIGHT,
            velocity.z
        );
        
        itemEntity.setDeltaMovement(jumpVelocity);
        itemEntity.hasImpulse = true;
    }

    private static void processJumpingOutputItems(ServerLevel level) {
        for (var entity : level.getAllEntities()) {
            if (entity instanceof ItemEntity item && 
                item.isInvulnerable()) {
                BlockPos pos = item.blockPosition();
                if (level.getFluidState(pos).is(Fluids.LAVA) || level.getFluidState(pos.below()).is(Fluids.LAVA)) {
                    performLavaJump(item);
                }
            }
        }
    }

    public static void addCustomRecipe(FluidConversionRecipe recipe) {
        customRecipes.put(recipe.getId(), recipe);
    }
    
    public static void removeCustomRecipe(ResourceLocation recipeId) {
        customRecipes.remove(recipeId);
    }
    
    public static java.util.Collection<FluidConversionRecipe> getCustomRecipes() {
        return customRecipes.values();
    }

    private static class ConversionData {
        UUID itemId;
        Player targetPlayer;
        FluidConversionRecipe recipe;
        ItemStack originalItemStack;
        Vec3 position;
        int tickCount;
        
        ConversionData(UUID itemId, Player targetPlayer, FluidConversionRecipe recipe, ItemStack originalItemStack, Vec3 position) {
            this.itemId = itemId;
            this.targetPlayer = targetPlayer;
            this.recipe = recipe;
            this.originalItemStack = originalItemStack;
            this.position = position;
            this.tickCount = 0;
        }
    }
}