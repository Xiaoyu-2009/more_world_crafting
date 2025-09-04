package com.xiaoyu.more_world_crafting.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaoyu.more_world_crafting.recipe.FluidFusionRecipe;
import com.xiaoyu.more_world_crafting.recipe.ModRecipeTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FluidFusionManager {
    private static final Map<String, Integer> activeFusions = new HashMap<>();
    private static final Map<ResourceLocation, FluidFusionRecipe> customRecipes = new HashMap<>();
    private static final double SEARCH_RADIUS = 3.0;

    public static void tick(Level level) {
        if (level.isClientSide()) return;

        List<ItemEntity> fluidItems = getFluidItems(level);
        
        Map<String, List<ItemEntity>> itemGroups = groupItemsByRegion(fluidItems);
        
        for (Map.Entry<String, List<ItemEntity>> entry : itemGroups.entrySet()) {
            String regionKey = entry.getKey();
            List<ItemEntity> items = entry.getValue();
            
            if (items.size() < 2) continue;
            
            FluidFusionRecipe recipe = findMatchingRecipe(level, items);
            if (recipe != null) {
                List<ItemEntity> fusionInputs = findItemsForRecipe(items, recipe.getIngredients());
                for (ItemEntity inputItem : fusionInputs) {
                    CraftingAPI.protectFluidFusionInput(inputItem);
                }
                
                Integer tickTime = activeFusions.get(regionKey);
                if (tickTime == null) {
                    tickTime = 0;
                }
                
                tickTime++;
                activeFusions.put(regionKey, tickTime);
                
                if (tickTime >= recipe.getFusionTime()) {
                    if (performFusion(level, items, recipe)) {
                        activeFusions.remove(regionKey);
                    }
                }
            } else {
                activeFusions.remove(regionKey);
            }
        }
        
        activeFusions.entrySet().removeIf(entry -> {
            Integer tickTime = entry.getValue();
            return tickTime > 1200;
        });
    }

    private static List<ItemEntity> getFluidItems(Level level) {
        AABB searchBox = new AABB(-100, -64, -100, 100, 320, 100);
        return level.getEntitiesOfClass(ItemEntity.class, searchBox, entity -> {
            if (entity.getItem().isEmpty() || entity.getAge() < 10) return false;
            
            BlockPos pos = entity.blockPosition();
            return !level.getFluidState(pos).isEmpty();
        });
    }
    
    private static Map<String, List<ItemEntity>> groupItemsByRegion(List<ItemEntity> items) {
        Map<String, List<ItemEntity>> groups = new HashMap<>();
        
        for (ItemEntity item : items) {
            int regionX = (int) Math.floor(item.getX() / 3.0);
            int regionY = (int) Math.floor(item.getY() / 3.0);
            int regionZ = (int) Math.floor(item.getZ() / 3.0);
            
            String regionKey = regionX + "," + regionY + "," + regionZ;
            groups.computeIfAbsent(regionKey, k -> new ArrayList<>()).add(item);
        }
        
        return groups;
    }
    
    private static FluidFusionRecipe findMatchingRecipe(Level level, List<ItemEntity> items) {
        List<FluidFusionRecipe> allRecipes = new ArrayList<>();
        
        List<FluidFusionRecipe> datapackRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.FLUID_FUSION.get());
        allRecipes.addAll(datapackRecipes);
        
        allRecipes.addAll(customRecipes.values());
        
        for (FluidFusionRecipe recipe : allRecipes) {
            if (canMakeRecipe(items, recipe.getIngredients()) && isInCorrectFluid(level, items, recipe)) {
                return recipe;
            }
        }
        
        return null;
    }
    
    private static boolean isInCorrectFluid(Level level, List<ItemEntity> items, FluidFusionRecipe recipe) {
        for (ItemEntity item : items) {
            BlockPos pos = item.blockPosition();
            if (!level.getFluidState(pos).is(recipe.getRequiredFluid())) {
                return false;
            }
        }
        return true;
    }
    
    private static int getRequiredCount(Ingredient ingredient, ItemStack stack) {
        ItemStack[] possibleItems = ingredient.getItems();
        if (possibleItems.length > 0) {
            for (ItemStack possibleItem : possibleItems) {
                if (ItemStack.isSameItemSameTags(stack, possibleItem)) {
                    return Math.max(1, possibleItem.getCount());
                }
            }
        }
        return 1;
    }
    
    private static boolean canMakeRecipe(List<ItemEntity> items, List<Ingredient> ingredients) {
        List<ItemStack> availableStacks = new ArrayList<>();
        for (ItemEntity item : items) {
            availableStacks.add(item.getItem().copy());
        }
        
        for (Ingredient ingredient : ingredients) {
            boolean found = false;
            for (int i = 0; i < availableStacks.size(); i++) {
                ItemStack stack = availableStacks.get(i);
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    int requiredCount = getRequiredCount(ingredient, stack);
                    
                    if (stack.getCount() >= requiredCount) {
                        stack.shrink(requiredCount);
                        if (stack.isEmpty()) {
                            availableStacks.remove(i);
                        }
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                return false;
            }
        }
        
        return true;
    }

    private static boolean performFusion(Level level, List<ItemEntity> items, FluidFusionRecipe recipe) {
        List<ItemEntity> itemsToConsume = findItemsForRecipe(items, recipe.getIngredients());
        if (itemsToConsume.size() != recipe.getIngredients().size()) {
            return false;
        }
        
        double centerX = 0, centerY = 0, centerZ = 0;
        for (ItemEntity item : itemsToConsume) {
            centerX += item.getX();
            centerY += item.getY();
            centerZ += item.getZ();
        }
        centerX /= itemsToConsume.size();
        centerY /= itemsToConsume.size();
        centerZ /= itemsToConsume.size();
        
        for (int i = 0; i < itemsToConsume.size(); i++) {
            ItemEntity item = itemsToConsume.get(i);
            Ingredient ingredient = recipe.getIngredients().get(i);
            
            int requiredCount = getRequiredCount(ingredient, item.getItem());
            
            ItemStack stack = item.getItem();
            stack.shrink(requiredCount);
            if (stack.isEmpty()) {
                item.discard();
            } else {
                item.setItem(stack);
            }
        }
        
        ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
        ItemEntity resultEntity = CraftingAPI.createOutputItem((net.minecraft.server.level.ServerLevel) level, new Vec3(centerX, centerY + 0.5, centerZ), result, null);
        level.addFreshEntity(resultEntity);
        
        return true;
    }

    private static List<ItemEntity> findItemsForRecipe(List<ItemEntity> items, List<Ingredient> ingredients) {
        List<ItemEntity> result = new ArrayList<>();
        List<ItemEntity> availableItems = new ArrayList<>(items);
        
        for (Ingredient ingredient : ingredients) {
            boolean found = false;
            for (int i = 0; i < availableItems.size(); i++) {
                ItemEntity item = availableItems.get(i);
                if (item != null && !item.getItem().isEmpty() && ingredient.test(item.getItem())) {
                    int requiredCount = getRequiredCount(ingredient, item.getItem());
                    
                    if (item.getItem().getCount() >= requiredCount) {
                        result.add(item);
                        availableItems.set(i, null);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                return new ArrayList<>();
            }
        }
        
        return result;
    }

    public static void addCustomRecipe(FluidFusionRecipe recipe) {
        customRecipes.put(recipe.getId(), recipe);
    }
    
    public static void removeCustomRecipe(ResourceLocation recipeId) {
        customRecipes.remove(recipeId);
    }
    
    public static java.util.Collection<FluidFusionRecipe> getCustomRecipes() {
        return customRecipes.values();
    }
}