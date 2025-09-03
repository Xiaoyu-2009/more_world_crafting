package com.xiaoyu.more_world_crafting.compat.crafttweaker.actions;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.xiaoyu.more_world_crafting.crafting.FluidFusionManager;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import com.xiaoyu.more_world_crafting.recipe.FluidFusionRecipe;

public class AddFluidFusionRecipeAction extends BaseRecipeAction {
    private final ResourceLocation recipeId;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final int fusionTime;
    private final Fluid requiredFluid;
    private final String fluidName;
    
    public AddFluidFusionRecipeAction(ResourceLocation recipeId, IItemStack[] ingredients, IItemStack result, 
    int fusionTime, String requiredFluid) {
        this.recipeId = recipeId;
        this.fluidName = requiredFluid;
        
        this.ingredients = NonNullList.withSize(ingredients.length, Ingredient.EMPTY);
        for (int i = 0; i < ingredients.length; i++) {
            this.ingredients.set(i, Ingredient.of(ingredients[i].getInternal()));
        }
        
        this.result = result.getInternal().copy();
        this.fusionTime = fusionTime;
        this.requiredFluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(requiredFluid));
        
        if (this.requiredFluid == null) {
            throw new IllegalArgumentException("Unknown fluid: " + requiredFluid);
        }
    }
    
    @Override
    public void apply() {
        FluidFusionRecipe recipe = new FluidFusionRecipe(recipeId, ingredients, result, fusionTime, requiredFluid);
        FluidFusionManager.addCustomRecipe(recipe);
    }
    
    @Override
    public String describe() {
        return String.format("Adding Fluid Fusion recipe with id %s: ingredients -> %s (time: %d, fluid: %s)", 
        recipeId, result.toString(), fusionTime, fluidName);
    }
    
    @Override
    public void undo() {
        FluidFusionManager.removeCustomRecipe(recipeId);
    }
    
    @Override
    public String describeUndo() {
        return String.format("Removing Fluid Fusion recipe with id %s", recipeId);
    }
}