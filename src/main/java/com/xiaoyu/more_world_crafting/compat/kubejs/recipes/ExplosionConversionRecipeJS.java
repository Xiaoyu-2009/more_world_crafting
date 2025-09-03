package com.xiaoyu.more_world_crafting.compat.kubejs.recipes;

import com.xiaoyu.more_world_crafting.compat.kubejs.components.MoreWorldCraftingComponents;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface ExplosionConversionRecipeJS {

    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");

    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");

    RecipeKey<Float> SUCCESS_CHANCE = MoreWorldCraftingComponents.CONVERSION_CHANCE;

    RecipeSchema SCHEMA = new RecipeSchema(INGREDIENT, RESULT, SUCCESS_CHANCE);
}