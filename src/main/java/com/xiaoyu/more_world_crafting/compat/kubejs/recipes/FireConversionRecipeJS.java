package com.xiaoyu.more_world_crafting.compat.kubejs.recipes;

import com.xiaoyu.more_world_crafting.compat.kubejs.components.MoreWorldCraftingComponents;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface FireConversionRecipeJS {

    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");

    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");

    RecipeKey<Float> CONVERSION_CHANCE = MoreWorldCraftingComponents.CONVERSION_CHANCE;

    RecipeKey<String> FIRE_TYPE = MoreWorldCraftingComponents.FIRE_TYPE;

    RecipeSchema SCHEMA = new RecipeSchema(INGREDIENT, RESULT, CONVERSION_CHANCE, FIRE_TYPE);
}