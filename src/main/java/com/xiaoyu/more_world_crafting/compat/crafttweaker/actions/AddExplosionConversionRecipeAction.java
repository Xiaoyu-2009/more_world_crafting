package com.xiaoyu.more_world_crafting.compat.crafttweaker.actions;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.xiaoyu.more_world_crafting.crafting.ExplosionConversionManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import com.xiaoyu.more_world_crafting.recipe.ExplosionConversionRecipe;

public class AddExplosionConversionRecipeAction extends BaseRecipeAction {
    private final ResourceLocation recipeId;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float successChance;
    
    public AddExplosionConversionRecipeAction(ResourceLocation recipeId, IItemStack ingredient, IItemStack result, 
    float successChance) {
        this.recipeId = recipeId;
        this.ingredient = Ingredient.of(ingredient.getInternal());
        this.result = result.getInternal().copy();
        this.successChance = successChance;
    }
    
    @Override
    public void apply() {
        ExplosionConversionRecipe recipe = new ExplosionConversionRecipe(recipeId, ingredient, result, successChance);
        ExplosionConversionManager.addCustomRecipe(recipe);
    }
    
    @Override
    public String describe() {
        return String.format("Adding Explosion Conversion recipe with id %s: %s -> %s (chance: %.2f)", 
        recipeId, ingredient.toJson(), result.toString(), successChance);
    }
    
    @Override
    public void undo() {
        ExplosionConversionManager.removeCustomRecipe(recipeId);
    }
    
    @Override
    public String describeUndo() {
        return String.format("Removing Explosion Conversion recipe with id %s", recipeId);
    }
}