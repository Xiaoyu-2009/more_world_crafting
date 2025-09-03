package com.xiaoyu.more_world_crafting.compat.crafttweaker.actions;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.xiaoyu.more_world_crafting.crafting.CrushingConversionManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import com.xiaoyu.more_world_crafting.recipe.CrushingConversionRecipe;

public class AddCrushingConversionRecipeAction extends BaseRecipeAction {
    private final ResourceLocation recipeId;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int minHeight;
    private final float conversionChance;
    private final String gravityType;
    
    public AddCrushingConversionRecipeAction(ResourceLocation recipeId, IItemStack ingredient, IItemStack result, 
    float conversionChance, int minHeight, String gravityType) {
        this.recipeId = recipeId;
        this.ingredient = Ingredient.of(ingredient.getInternal());
        this.result = result.getInternal().copy();
        this.conversionChance = conversionChance;
        this.minHeight = minHeight;
        this.gravityType = gravityType;
    }
    
    public AddCrushingConversionRecipeAction(ResourceLocation recipeId, IItemStack ingredient, IItemStack result, 
    float conversionChance) {
        this(recipeId, ingredient, result, conversionChance, 3, "minecraft:anvil");
    }
    
    @Override
    public void apply() {
        CrushingConversionRecipe recipe = new CrushingConversionRecipe(
            recipeId, ingredient, result, minHeight, conversionChance, gravityType
        );
        CrushingConversionManager.addCustomRecipe(recipe);
    }
    
    @Override
    public String describe() {
        return String.format(
        "Adding Crushing Conversion recipe with id %s: %s -> %s (chance: %.2f, height: %d, gravity: %s)", 
        recipeId, ingredient.toJson(), result.toString(), conversionChance, minHeight, gravityType);
    }
    
    @Override
    public void undo() {
        CrushingConversionManager.removeCustomRecipe(recipeId);
    }
    
    @Override
    public String describeUndo() {
        return String.format("Removing Crushing Conversion recipe with id %s", recipeId);
    }
}