package com.xiaoyu.more_world_crafting.compat.crafttweaker.managers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.resources.ResourceLocation;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.actions.AddFluidConversionRecipeAction;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.more_world_crafting.FluidConversionManager")
@Document("mods/more_world_crafting/FluidConversionManager")
public class FluidConversionManager {

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack ingredient, IItemStack result, float successChance, String requiredFluid) {
        ResourceLocation recipeId = new ResourceLocation(MoreWorldCrafting.MODID, "crafttweaker/" + name);
        CraftTweakerAPI.apply(new AddFluidConversionRecipeAction(recipeId, ingredient, result, successChance, requiredFluid));
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack ingredient, IItemStack result, String requiredFluid) {
        addRecipe(name, ingredient, result, 1.0f, requiredFluid);
    }
}