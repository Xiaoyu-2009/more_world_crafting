package com.xiaoyu.more_world_crafting.compat.crafttweaker.actions;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.xiaoyu.more_world_crafting.crafting.FireFusionManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import com.xiaoyu.more_world_crafting.recipe.FireFusionRecipe;

public class AddFireFusionRecipeAction extends BaseRecipeAction {
    private final ResourceLocation recipeId;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float successChance;
    private final FireFusionRecipe.FireType fireType;
    private final String fireTypeString;
    
    public AddFireFusionRecipeAction(ResourceLocation recipeId, IItemStack ingredient, IItemStack result, 
    float successChance, String fireType) {
        this.recipeId = recipeId;
        this.ingredient = Ingredient.of(ingredient.getInternal());
        this.result = result.getInternal().copy();
        this.successChance = successChance;
        this.fireTypeString = fireType;
        this.fireType = FireFusionRecipe.FireType.fromItemId(fireType);
    }
    
    @Override
    public void apply() {
        FireFusionRecipe recipe = new FireFusionRecipe(recipeId, ingredient, result, successChance, fireType);
        FireFusionManager.addCustomRecipe(recipe);
    }
    
    @Override
    public String describe() {
        return String.format("Adding Fire Fusion recipe with id %s: %s -> %s (chance: %.2f, fire: %s)", 
        recipeId, ingredient.toJson(), result.toString(), successChance, fireTypeString);
    }
    
    @Override
    public void undo() {
        FireFusionManager.removeCustomRecipe(recipeId);
    }
    
    @Override
    public String describeUndo() {
        return String.format("Removing Fire Fusion recipe with id %s", recipeId);
    }
}