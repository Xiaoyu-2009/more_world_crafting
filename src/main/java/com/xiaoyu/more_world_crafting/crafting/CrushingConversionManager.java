package com.xiaoyu.more_world_crafting.crafting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import com.xiaoyu.more_world_crafting.recipe.CrushingConversionRecipe;
import com.xiaoyu.more_world_crafting.recipe.ModRecipeTypes;

public class CrushingConversionManager {
    private static final Map<UUID, FallingBlockData> TRACKED_FALLING_BLOCKS = new HashMap<>();
    private static final Map<ResourceLocation, CrushingConversionRecipe> customRecipes = new HashMap<>();

    private static final double COLLISION_HORIZONTAL_RADIUS = 0.8;
    private static final double COLLISION_VERTICAL_RADIUS = 1.5;
    private static final double MIN_FALL_VELOCITY = 0.1;
    private static final int TICK_INTERVAL = 2;

    private static class FallingBlockData {
        public final String dimension;
        public final double startY;
        public final double startX;
        public final double startZ;
        public boolean processed;

        public FallingBlockData(String dimension, double startY, double startX, double startZ) {
            this.dimension = dimension;
            this.startY = startY;
            this.startX = startX;
            this.startZ = startZ;
            this.processed = false;
        }
    }

    public static void tick(Level level) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (level.getGameTime() % TICK_INTERVAL != 0) {
            return;
        }

        trackNewFallingBlocks(serverLevel);
        processTrackedBlocks(serverLevel);
    }

    private static void trackNewFallingBlocks(ServerLevel level) {
        for (var entity : level.getAllEntities()) {
            if (!(entity instanceof FallingBlockEntity fallingBlock)) {
                continue;
            }

            UUID blockId = fallingBlock.getUUID();
            if (!TRACKED_FALLING_BLOCKS.containsKey(blockId) && isValidCrushingBlock(fallingBlock)) {
                TRACKED_FALLING_BLOCKS.put(blockId, new FallingBlockData(
                    level.dimension().location().toString(),
                    fallingBlock.getY(),
                    fallingBlock.getX(),
                    fallingBlock.getZ()
                ));
            }
        }
    }

    private static boolean isValidCrushingBlock(FallingBlockEntity fallingBlock) {
        return !fallingBlock.isRemoved() && fallingBlock.isAlive();
    }

    private static void processTrackedBlocks(ServerLevel level) {
        Iterator<Map.Entry<UUID, FallingBlockData>> iterator = TRACKED_FALLING_BLOCKS.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, FallingBlockData> entry = iterator.next();
            UUID blockId = entry.getKey();
            FallingBlockData fallData = entry.getValue();

            if (!isDimensionMatch(fallData, level)) {
                continue;
            }

            FallingBlockEntity fallingBlock = findFallingBlock(level, blockId);

            if (fallingBlock == null) {
                iterator.remove();
                continue;
            }

            boolean shouldRemove = processBlockConversion(level, fallingBlock, fallData);

            if (shouldRemove) {
                iterator.remove();
            }
        }
    }

    private static boolean isDimensionMatch(FallingBlockData fallData, ServerLevel level) {
        return fallData.dimension.equals(level.dimension().location().toString());
    }

    private static FallingBlockEntity findFallingBlock(ServerLevel level, UUID blockId) {
        for (var entity : level.getAllEntities()) {
            if (entity instanceof FallingBlockEntity fallingBlock &&
                fallingBlock.getUUID().equals(blockId) &&
                !fallingBlock.isRemoved()) {
                return fallingBlock;
            }
        }
        return null;
    }

    private static boolean processBlockConversion(ServerLevel level, FallingBlockEntity fallingBlock, FallingBlockData fallData) {
        if (fallData.processed) {
            return true;
        }

        double velocity = Math.abs(fallingBlock.getDeltaMovement().y);

        boolean isOnGround = fallingBlock.onGround();
        boolean hasMinimumVelocity = velocity >= MIN_FALL_VELOCITY;

        if (!isOnGround && !hasMinimumVelocity) {
            return false;
        }

        List<ItemEntity> nearbyItems = findNearbyItems(level, fallingBlock);

        for (ItemEntity item : nearbyItems) {
            CrushingConversionRecipe recipe = findMatchingRecipe(level, item, fallingBlock);

            if (recipe != null) {
                double itemY = item.getY();
                double totalHeight = Math.ceil(fallData.startY) - Math.floor(itemY) + 1;
                if (totalHeight >= recipe.getMinHeight()) {
                    executeConversion(level, item, recipe);
                    fallData.processed = true;
                    return true;
                }
            }
        }

        return isOnGround;
    }

    private static List<ItemEntity> findNearbyItems(ServerLevel level, FallingBlockEntity fallingBlock) {
        List<ItemEntity> nearbyItems = new ArrayList<>();
        Vec3 blockPos = fallingBlock.position();

        for (var entity : level.getAllEntities()) {
            if (!(entity instanceof ItemEntity item) ||
                item.isRemoved() ||
                item.getItem().isEmpty()) {
                continue;
            }

            Vec3 itemPos = item.position();

            double horizontalDistance = Math.sqrt(
                Math.pow(blockPos.x - itemPos.x, 2) +
                Math.pow(blockPos.z - itemPos.z, 2)
            );
            double verticalDistance = Math.abs(blockPos.y - itemPos.y);

            if (horizontalDistance <= COLLISION_HORIZONTAL_RADIUS &&
                verticalDistance <= COLLISION_VERTICAL_RADIUS) {
                nearbyItems.add(item);
            }
        }

        return nearbyItems;
    }

    private static CrushingConversionRecipe findMatchingRecipe(ServerLevel level, ItemEntity item, FallingBlockEntity fallingBlock) {
        ItemStack itemStack = item.getItem();
        BlockState blockState = fallingBlock.getBlockState();
        Item blockItem = blockState.getBlock().asItem();
        ResourceLocation blockItemId = BuiltInRegistries.ITEM.getKey(blockItem);

        List<CrushingConversionRecipe> allRecipes = getAllRecipes(level);

        for (CrushingConversionRecipe recipe : allRecipes) {
            if (recipe.getIngredient().test(itemStack)) {
                String recipeGravityType = recipe.getGravityType();
                if (recipeGravityType.equals(blockItemId.toString())) {
                    return recipe;
                }
            }
        }

        return null;
    }

    private static List<CrushingConversionRecipe> getAllRecipes(ServerLevel level) {
        List<CrushingConversionRecipe> allRecipes = new ArrayList<>();

        List<CrushingConversionRecipe> registeredRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.CRUSHING_CONVERSION.get());
        allRecipes.addAll(registeredRecipes);
        allRecipes.addAll(customRecipes.values());

        return allRecipes;
    }

    private static void executeConversion(ServerLevel level, ItemEntity originalItem, CrushingConversionRecipe recipe) {
        ItemStack inputStack = originalItem.getItem();
        int inputCount = inputStack.getCount();

        int successfulConversions = CraftingAPI.calculateSuccessfulConversions(inputCount, recipe.getConversionChance(), level);

        if (successfulConversions <= 0) {
            return;
        }

        ItemStack outputStack = recipe.getResultItem(level.registryAccess()).copy();

        int outputCount = outputStack.getCount();
        outputStack.setCount(outputCount * successfulConversions);

        Vec3 position = originalItem.position();

        originalItem.discard();

        ItemEntity resultItem = CraftingAPI.createOutputItemWithJump(level, position, outputStack, originalItem, 0.3);
        level.addFreshEntity(resultItem);
    }

    public static void addCustomRecipe(CrushingConversionRecipe recipe) {
        customRecipes.put(recipe.getId(), recipe);
    }

    public static void removeCustomRecipe(ResourceLocation recipeId) {
        customRecipes.remove(recipeId);
    }

    public static java.util.Collection<CrushingConversionRecipe> getCustomRecipes() {
        return customRecipes.values();
    }
}