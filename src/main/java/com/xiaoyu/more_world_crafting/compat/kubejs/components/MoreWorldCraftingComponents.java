package com.xiaoyu.more_world_crafting.compat.kubejs.components;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;

public class MoreWorldCraftingComponents {

    public static final RecipeKey<Integer> MIN_HEIGHT = NumberComponent.INT.key("min_height").optional(3);
    
    public static final RecipeKey<Float> CONVERSION_CHANCE = NumberComponent.FLOAT.key("conversion_chance").optional(1.0f);
    
    public static final RecipeKey<String> GRAVITY_TYPE = StringComponent.ANY.key("gravity_type").optional("minecraft:sand");
    
    public static final RecipeKey<Float> SUCCESS_CHANCE = NumberComponent.FLOAT.key("success_chance").optional(1.0f);
    
    public static final RecipeKey<String> FIRE_TYPE = StringComponent.ANY.key("fire_type").optional("minecraft:fire");
    
    public static final RecipeKey<String> REQUIRED_FLUID = StringComponent.ANY.key("required_fluid");
    
    public static final RecipeKey<Integer> FUSION_TIME = NumberComponent.INT.key("fusion_time").optional(60);
    
    public static final RecipeKey<Float> LIGHTNING_CHANCE = NumberComponent.FLOAT.key("lightning_chance").optional(0.1f);
}