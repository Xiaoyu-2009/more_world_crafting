package com.xiaoyu.more_world_crafting.compat.jei;

import com.xiaoyu.more_world_crafting.compat.jei.category.*;
import com.xiaoyu.more_world_crafting.crafting.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.recipe.CrushingConversionRecipe;
import com.xiaoyu.more_world_crafting.recipe.ExplosionConversionRecipe;
import com.xiaoyu.more_world_crafting.recipe.FireFusionRecipe;
import com.xiaoyu.more_world_crafting.recipe.FluidConversionRecipe;
import com.xiaoyu.more_world_crafting.recipe.LightningConversionRecipe;
import com.xiaoyu.more_world_crafting.recipe.ModRecipeTypes;
import com.xiaoyu.more_world_crafting.recipe.VoidFusionRecipe;
import com.xiaoyu.more_world_crafting.recipe.FluidFusionRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIMoreWorldCraftingPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MoreWorldCrafting.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FluidFusionCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FluidConversionCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new VoidFusionCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FireFusionCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new LightningConversionCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ExplosionConversionCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CrushingConversionCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        
        List<FluidFusionRecipe> fluidFusionRecipes = new ArrayList<>(recipeManager.getAllRecipesFor(ModRecipeTypes.FLUID_FUSION.get()));
        fluidFusionRecipes.addAll(FluidFusionManager.getCustomRecipes());
        registration.addRecipes(FluidFusionCategory.FLUID_FUSION_TYPE, fluidFusionRecipes);
        
        List<FluidConversionRecipe> fluidConversionRecipes = new ArrayList<>(recipeManager.getAllRecipesFor(ModRecipeTypes.FLUID_CONVERSION.get()));
        fluidConversionRecipes.addAll(FluidConversionManager.getCustomRecipes());
        registration.addRecipes(FluidConversionCategory.FLUID_CONVERSION_TYPE, fluidConversionRecipes);
        
        List<VoidFusionRecipe> voidFusionRecipes = new ArrayList<>(recipeManager.getAllRecipesFor(ModRecipeTypes.VOID_FUSION.get()));
        voidFusionRecipes.addAll(VoidFusionManager.getCustomRecipes());
        registration.addRecipes(VoidFusionCategory.VOID_FUSION_TYPE, voidFusionRecipes);
        
        List<FireFusionRecipe> fireFusionRecipes = new ArrayList<>(recipeManager.getAllRecipesFor(ModRecipeTypes.FIRE_FUSION.get()));
        fireFusionRecipes.addAll(FireFusionManager.getCustomRecipes());
        registration.addRecipes(FireFusionCategory.FIRE_FUSION_TYPE, fireFusionRecipes);
        
        List<LightningConversionRecipe> lightningConversionRecipes = new ArrayList<>(recipeManager.getAllRecipesFor(ModRecipeTypes.LIGHTNING_CONVERSION.get()));
        lightningConversionRecipes.addAll(LightningConversionManager.getCustomRecipes());
        registration.addRecipes(LightningConversionCategory.LIGHTNING_CONVERSION_TYPE, lightningConversionRecipes);
        
        List<ExplosionConversionRecipe> explosionConversionRecipes = new ArrayList<>(recipeManager.getAllRecipesFor(ModRecipeTypes.EXPLOSION_CONVERSION.get()));
        explosionConversionRecipes.addAll(ExplosionConversionManager.getCustomRecipes());
        registration.addRecipes(ExplosionConversionCategory.EXPLOSION_CONVERSION_TYPE, explosionConversionRecipes);
        
        List<CrushingConversionRecipe> crushingConversionRecipes = new ArrayList<>(recipeManager.getAllRecipesFor(ModRecipeTypes.CRUSHING_CONVERSION.get()));
        crushingConversionRecipes.addAll(CrushingConversionManager.getCustomRecipes());
        registration.addRecipes(CrushingConversionCategory.CRUSHING_CONVERSION_TYPE, crushingConversionRecipes);
    }
}