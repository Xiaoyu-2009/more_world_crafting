package com.xiaoyu.more_world_crafting.compat.crafttweaker.managers;

import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.resources.ResourceLocation;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.actions.AddLightningConversionRecipeAction;

@ZenRegister
@ZenCodeType.Name("mods.more_world_crafting.LightningConversionManager")
@Document("mods/more_world_crafting/LightningConversionManager")
public class LightningConversionManager {

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack ingredient, IItemStack result, float conversionChance, float lightningChance) {
        ResourceLocation recipeId = new ResourceLocation(MoreWorldCrafting.MODID, "crafttweaker/" + name);
        CraftTweakerAPI.apply(new AddLightningConversionRecipeAction(recipeId, ingredient, result, conversionChance, lightningChance));
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack ingredient, IItemStack result, float conversionChance) {
        addRecipe(name, ingredient, result, conversionChance, 0.1f);
    }
}