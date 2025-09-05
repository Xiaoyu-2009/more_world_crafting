package com.xiaoyu.more_world_crafting.compat.crafttweaker.actions;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.xiaoyu.more_world_crafting.crafting.VoidConversionManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import com.xiaoyu.more_world_crafting.recipe.VoidConversionRecipe;

public class AddVoidConversionRecipeAction extends BaseRecipeAction {
    private final ResourceLocation recipeId;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float successChance;
    
    public AddVoidConversionRecipeAction(ResourceLocation recipeId, IItemStack ingredient, IItemStack result, 
    float successChance) {
        this.recipeId = recipeId;
        this.ingredient = Ingredient.of(ingredient.getInternal());
        this.result = result.getInternal().copy();
        this.successChance = successChance;
    }
    
    @Override
    public void apply() {
        VoidConversionRecipe recipe = new VoidConversionRecipe(recipeId, ingredient, result, successChance);
        VoidConversionManager.addCustomRecipe(recipe);
    }
    
    @Override
    public String describe() {
        return String.format("Adding Void Conversion recipe with id %s: %s -> %s (chance: %.2f)", 
        recipeId, ingredient.toJson(), result.toString(), successChance);
    }
    
    @Override
    public void undo() {
        VoidConversionManager.removeCustomRecipe(recipeId);
    }
    
    @Override
    public String describeUndo() {
        return String.format("Removing Void Conversion recipe with id %s", recipeId);
    }
}