package com.xiaoyu.more_world_crafting.compat.crafttweaker.managers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.resources.ResourceLocation;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.actions.AddFireConversionRecipeAction;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.more_world_crafting.FireConversionManager")
@Document("mods/more_world_crafting/FireConversionManager")
public class FireConversionManager {

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack ingredient, IItemStack result, float successChance, String fireType) {
        ResourceLocation recipeId = new ResourceLocation(MoreWorldCrafting.MODID, "crafttweaker/" + name);
        CraftTweakerAPI.apply(new AddFireConversionRecipeAction(recipeId, ingredient, result, successChance, fireType));
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack ingredient, IItemStack result, String fireType) {
        addRecipe(name, ingredient, result, 1.0f, fireType);
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack ingredient, IItemStack result, float successChance) {
        addRecipe(name, ingredient, result, successChance, "minecraft:fire");
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack ingredient, IItemStack result) {
        addRecipe(name, ingredient, result, 1.0f, "minecraft:fire");
    }
}