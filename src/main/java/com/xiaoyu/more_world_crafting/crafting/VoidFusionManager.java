package com.xiaoyu.more_world_crafting.crafting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

import com.xiaoyu.more_world_crafting.recipe.ModRecipeTypes;
import com.xiaoyu.more_world_crafting.recipe.VoidFusionRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class VoidFusionManager {
    private static final Map<UUID, ConversionData> pendingConversions = new HashMap<>();
    private static final Map<ResourceLocation, VoidFusionRecipe> customRecipes = new HashMap<>();
    private static final double VOID_TELEPORT_HEIGHT = 10.0;
    private static final int CONVERSION_DELAY = 10;
    private static final double VOID_THRESHOLD = -65.0;

    public static void tick(Level level) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        for (var entity : serverLevel.getAllEntities()) {
            if (!(entity instanceof ItemEntity item)) continue;
            if (item.isRemoved() || item.getItem().isEmpty() || !isInVoid(item.position())) continue;
            
            VoidFusionRecipe recipe = findMatchingRecipe(serverLevel, item.getItem());
            if (recipe != null) {
                UUID itemId = item.getUUID();
                if (!pendingConversions.containsKey(itemId)) {
                    Player targetPlayer = getItemThrower(item);
                    pendingConversions.put(itemId, new ConversionData(itemId, targetPlayer, recipe, item.getItem().copy(), item.position()));
                }
            }
        }

        processConversions(serverLevel);
    }

    private static void processConversions(ServerLevel level) {
        Iterator<Map.Entry<UUID, ConversionData>> iterator = pendingConversions.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<UUID, ConversionData> entry = iterator.next();
            ConversionData data = entry.getValue();
            
            data.tickCount++;
            
            if (data.tickCount >= CONVERSION_DELAY) {
                boolean itemFound = false;
                for (var entity : level.getAllEntities()) {
                    if (entity instanceof ItemEntity item && item.getUUID().equals(data.itemId) && !item.isRemoved()) {
                        item.discard();
                        itemFound = true;
                        break;
                    }
                }
                
                if (itemFound) {
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

    private static VoidFusionRecipe findMatchingRecipe(ServerLevel level, ItemStack itemStack) {
        List<VoidFusionRecipe> allRecipes = new ArrayList<>();
        
        List<VoidFusionRecipe> datapackRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.VOID_FUSION.get());
        allRecipes.addAll(datapackRecipes);
        
        allRecipes.addAll(customRecipes.values());
        
        for (VoidFusionRecipe recipe : allRecipes) {
            if (recipe.getIngredient().test(itemStack)) {
                return recipe;
            }
        }
        
        return null;
    }

    private static boolean performConversion(ServerLevel level, ItemStack originalStack, VoidFusionRecipe recipe, Player targetPlayer, Vec3 position) {
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

        if (targetPlayer != null && targetPlayer.isAlive()) {
            boolean added = targetPlayer.getInventory().add(result);
            
            if (added) {
                level.playSound(null, targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(), 
                SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, 
                ((level.random.nextFloat() - level.random.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            } else {
                ItemEntity dropEntity = CraftingAPI.createOutputItem(level, new Vec3(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ()), result, null);
                level.addFreshEntity(dropEntity);
            }
        } else {
            Vec3 teleportPosition = new Vec3(position.x, position.y + VOID_TELEPORT_HEIGHT, position.z);
            ItemEntity resultEntity = CraftingAPI.createOutputItem(level, teleportPosition, result, null);
            level.addFreshEntity(resultEntity);
        }
        
        return true;
    }

    private static boolean isInVoid(Vec3 position) {
        return position.y < VOID_THRESHOLD;
    }

    public static void addCustomRecipe(VoidFusionRecipe recipe) {
        customRecipes.put(recipe.getId(), recipe);
    }
    
    public static void removeCustomRecipe(ResourceLocation recipeId) {
        customRecipes.remove(recipeId);
    }
    
    public static java.util.Collection<VoidFusionRecipe> getCustomRecipes() {
        return customRecipes.values();
    }

    private static class ConversionData {
        UUID itemId;
        Player targetPlayer;
        VoidFusionRecipe recipe;
        ItemStack originalItemStack;
        Vec3 position;
        int tickCount;
        
        ConversionData(UUID itemId, Player targetPlayer, VoidFusionRecipe recipe, ItemStack originalItemStack, Vec3 position) {
            this.itemId = itemId;
            this.targetPlayer = targetPlayer;
            this.recipe = recipe;
            this.originalItemStack = originalItemStack;
            this.position = position;
            this.tickCount = 0;
        }
    }
}