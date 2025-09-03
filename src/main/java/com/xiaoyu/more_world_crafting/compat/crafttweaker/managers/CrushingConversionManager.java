package com.xiaoyu.more_world_crafting.compat.crafttweaker.managers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.resources.ResourceLocation;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.actions.AddCrushingConversionRecipeAction;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.more_world_crafting.CrushingConversionManager")
@Document("mods/more_world_crafting/CrushingConversionManager")
public class CrushingConversionManager {
    
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack ingredient, IItemStack result, float conversionChance, int minHeight, String gravityType) {
        ResourceLocation recipeId = new ResourceLocation(MoreWorldCrafting.MODID, "crafttweaker/" + name);
        CraftTweakerAPI.apply(new AddCrushingConversionRecipeAction(recipeId, ingredient, result, conversionChance, minHeight, gravityType));
    }
    
    @ZenCodeType.Method
    public void addRecipe(String name, IItemStack ingredient, IItemStack result, float conversionChance) {
        addRecipe(name, ingredient, result, conversionChance, 3, "minecraft:anvil");
    }
}