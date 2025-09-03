package com.xiaoyu.more_world_crafting.compat.kubejs.recipes;

import com.xiaoyu.more_world_crafting.compat.kubejs.components.MoreWorldCraftingComponents;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface FluidFusionRecipeJS {

    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.INPUT.asArray().key("ingredients");

    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");

    RecipeKey<Integer> FUSION_TIME = MoreWorldCraftingComponents.FUSION_TIME;

    RecipeKey<String> REQUIRED_FLUID = MoreWorldCraftingComponents.REQUIRED_FLUID;

    RecipeSchema SCHEMA = new RecipeSchema(INGREDIENTS, RESULT, REQUIRED_FLUID, FUSION_TIME);
}