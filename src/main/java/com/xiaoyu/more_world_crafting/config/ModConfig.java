package com.xiaoyu.more_world_crafting.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ModConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue LIGHTNING_CONVERSION_ATTRACTS_LIGHTNING;
    public static final ForgeConfigSpec.BooleanValue THUNDERSTORM_DETECTION;
    public static final ForgeConfigSpec.BooleanValue OUTPUT_ITEM_GLOW_ENABLED;
    public static final ForgeConfigSpec.BooleanValue OUTPUT_ITEM_NAME_DISPLAY_ENABLED;
    public static final ForgeConfigSpec.BooleanValue OUTPUT_ITEM_SPAWN_JUMP_ENABLED;
    public static final ForgeConfigSpec.BooleanValue FLUID_FUSION_INVULNERABLE_ENABLED;

    static {
        BUILDER.push("Lightning Conversion Config");
        LIGHTNING_CONVERSION_ATTRACTS_LIGHTNING = BUILDER
                .comment(" Whether lightning conversion will attract lightning")
                .define("lightning_conversion_attracts_lightning", true);
        THUNDERSTORM_DETECTION = BUILDER
                .comment(" Thunderstorm detection")
                .define("thunderstorm_detection", true);
        BUILDER.pop();

        BUILDER.push("General Output Item Config");
        OUTPUT_ITEM_GLOW_ENABLED = BUILDER
                .comment(" Whether output items will glow")
                .define("output_item_glow_enabled", true);
        
        OUTPUT_ITEM_NAME_DISPLAY_ENABLED = BUILDER
                .comment(" Whether output items will display names")
                .define("output_item_name_display_enabled", true);
        
        OUTPUT_ITEM_SPAWN_JUMP_ENABLED = BUILDER
                .comment(" Whether output items will spawn with jump")
                .define("output_item_spawn_jump_enabled", true);
        BUILDER.pop();

        BUILDER.push("Fluid Fusion Config");
        FLUID_FUSION_INVULNERABLE_ENABLED = BUILDER
                .comment(" Whether input items in fluid fusion are invulnerable")
                .define("fluid_fusion_input_items_invulnerable", true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(Type.COMMON, SPEC);
    }
}