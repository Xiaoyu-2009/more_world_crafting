package com.xiaoyu.more_world_crafting.compat.crafttweaker.actions;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.xiaoyu.more_world_crafting.crafting.FluidConversionManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import com.xiaoyu.more_world_crafting.recipe.FluidConversionRecipe;

public class AddFluidConversionRecipeAction extends BaseRecipeAction {
    private final ResourceLocation recipeId;
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float successChance;
    private final Fluid requiredFluid;
    private final String fluidName;
    
    public AddFluidConversionRecipeAction(ResourceLocation recipeId, IItemStack ingredient, IItemStack result, 
    float successChance, String requiredFluid) {
        this.recipeId = recipeId;
        this.ingredient = Ingredient.of(ingredient.getInternal());
        this.result = result.getInternal().copy();
        this.successChance = successChance;
        this.fluidName = requiredFluid;
        this.requiredFluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(requiredFluid));
        
        if (this.requiredFluid == null) {
            throw new IllegalArgumentException("Unknown fluid: " + requiredFluid);
        }
    }
    
    @Override
    public void apply() {
        FluidConversionRecipe recipe = new FluidConversionRecipe(recipeId, ingredient, result, successChance, requiredFluid);
        FluidConversionManager.addCustomRecipe(recipe);
    }
    
    @Override
    public String describe() {
        return String.format("Adding Fluid Conversion recipe with id %s: %s -> %s (chance: %.2f, fluid: %s)", 
        recipeId, ingredient.toJson(), result.toString(), successChance, fluidName);
    }
    
    @Override
    public void undo() {
        FluidConversionManager.removeCustomRecipe(recipeId);
    }
    
    @Override
    public String describeUndo() {
        return String.format("Removing Fluid Conversion recipe with id %s", recipeId);
    }
}