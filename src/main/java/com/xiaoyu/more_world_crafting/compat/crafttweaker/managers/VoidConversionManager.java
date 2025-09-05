package com.xiaoyu.more_world_crafting.compat.crafttweaker.managers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.resources.ResourceLocation;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.actions.AddVoidConversionRecipeAction;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.more_world_crafting.VoidConversionManager")
@Document("mods/more_world_crafting/VoidConversionManager")
public class VoidConversionManager {

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack ingredient, IItemStack result, float successChance) {
        ResourceLocation recipeId = new ResourceLocation(MoreWorldCrafting.MODID, "crafttweaker/" + name);
        CraftTweakerAPI.apply(new AddVoidConversionRecipeAction(recipeId, ingredient, result, successChance));
    }

    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack ingredient, IItemStack result) {
        addRecipe(name, ingredient, result, 1.0f);
    }
}