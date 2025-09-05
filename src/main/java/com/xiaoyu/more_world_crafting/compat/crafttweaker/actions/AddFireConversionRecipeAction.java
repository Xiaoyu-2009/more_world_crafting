package com.xiaoyu.more_world_crafting.compat.crafttweaker.actions;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.xiaoyu.more_world_crafting.crafting.FireConversionManager;
import com.xiaoyu.more_world_crafting.recipe.FireConversionRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class AddFireConversionRecipeAction extends BaseRecipeAction {
    private final ResourceLocation recipeId;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float successChance;
    private final FireConversionRecipe.FireType fireType;
    private final String fireTypeString;
    
    public AddFireConversionRecipeAction(ResourceLocation recipeId, IItemStack ingredient, IItemStack result, 
    float successChance, String fireType) {
        this.recipeId = recipeId;
        this.ingredient = Ingredient.of(ingredient.getInternal());
        this.result = result.getInternal().copy();
        this.successChance = successChance;
        this.fireTypeString = fireType;
        this.fireType = FireConversionRecipe.FireType.fromItemId(fireType);
    }
    
    @Override
    public void apply() {
        FireConversionRecipe recipe = new FireConversionRecipe(recipeId, ingredient, result, successChance, fireType);
        FireConversionManager.addCustomRecipe(recipe);
    }
    
    @Override
    public String describe() {
        return String.format("Adding Fire Conversion recipe with id %s: %s -> %s (chance: %.2f, fire: %s)", 
        recipeId, ingredient.toJson(), result.toString(), successChance, fireTypeString);
    }
    
    @Override
    public void undo() {
        FireConversionManager.removeCustomRecipe(recipeId);
    }
    
    @Override
    public String describeUndo() {
        return String.format("Removing Fire Conversion recipe with id %s", recipeId);
    }
}