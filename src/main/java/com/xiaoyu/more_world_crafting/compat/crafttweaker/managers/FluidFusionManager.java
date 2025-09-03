package com.xiaoyu.more_world_crafting.compat.crafttweaker.managers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.resources.ResourceLocation;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.actions.AddFluidFusionRecipeAction;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.more_world_crafting.FluidFusionManager")
@Document("mods/more_world_crafting/FluidFusionManager")
public class FluidFusionManager {

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack[] ingredients, IItemStack result, int fusionTime, String requiredFluid) {
        ResourceLocation recipeId = new ResourceLocation(MoreWorldCrafting.MODID, "crafttweaker/" + name);
        CraftTweakerAPI.apply(new AddFluidFusionRecipeAction(recipeId, ingredients, result, fusionTime, requiredFluid));
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack[] ingredients, IItemStack result, String requiredFluid) {
        addRecipe(name, ingredients, result, 60, requiredFluid);
    }
}