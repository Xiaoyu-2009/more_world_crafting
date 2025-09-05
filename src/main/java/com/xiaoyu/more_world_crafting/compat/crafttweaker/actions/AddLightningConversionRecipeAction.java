package com.xiaoyu.more_world_crafting.compat.crafttweaker.actions;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.xiaoyu.more_world_crafting.crafting.LightningConversionManager;
import com.xiaoyu.more_world_crafting.recipe.LightningConversionRecipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class AddLightningConversionRecipeAction extends BaseRecipeAction {
    private final ResourceLocation recipeId;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float lightningChance;
    private final float conversionChance;
    private final String weatherId;
    
    public AddLightningConversionRecipeAction(ResourceLocation recipeId, IItemStack ingredient, IItemStack result, 
    float conversionChance, float lightningChance, String weatherId) {
        this.recipeId = recipeId;
        this.ingredient = Ingredient.of(ingredient.getInternal());
        this.result = result.getInternal().copy();
        this.conversionChance = conversionChance;
        this.lightningChance = lightningChance;
        this.weatherId = weatherId != null ? weatherId : "";
    }
    
    public AddLightningConversionRecipeAction(ResourceLocation recipeId, IItemStack ingredient, IItemStack result, 
    float conversionChance, float lightningChance) {
        this(recipeId, ingredient, result, conversionChance, lightningChance, "");
    }
    
    public AddLightningConversionRecipeAction(ResourceLocation recipeId, IItemStack ingredient, 
    IItemStack result, float conversionChance) {
        this(recipeId, ingredient, result, conversionChance, 0.1f, "");
    }
    
    @Override
    public void apply() {
        LightningConversionRecipe recipe = new LightningConversionRecipe(
            recipeId, ingredient, result, lightningChance, conversionChance, weatherId
            );
        LightningConversionManager.addCustomRecipe(recipe);
    }
    
    @Override
    public String describe() {
        return String.format(
        "Adding Lightning Conversion recipe with id %s: %s -> %s (conversion: %.2f, lightning: %.2f, weather: %s)", 
        recipeId, ingredient.toJson(), result.toString(), conversionChance, lightningChance, weatherId);
    }
    
    @Override
    public void undo() {
        LightningConversionManager.removeCustomRecipe(recipeId);
    }
    
    @Override
    public String describeUndo() {
        return String.format("Removing Lightning Conversion recipe with id %s", recipeId);
    }
}