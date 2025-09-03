package com.xiaoyu.more_world_crafting.compat.kubejs;

import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.compat.kubejs.recipes.*;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import net.minecraft.resources.ResourceLocation;

public class MoreWorldCraftingKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(new ResourceLocation(MoreWorldCrafting.MODID, "crushing_conversion"), CrushingConversionRecipeJS.SCHEMA);
        event.register(new ResourceLocation(MoreWorldCrafting.MODID, "explosion_conversion"), ExplosionConversionRecipeJS.SCHEMA);
        event.register(new ResourceLocation(MoreWorldCrafting.MODID, "fire_fusion"), FireFusionRecipeJS.SCHEMA);
        event.register(new ResourceLocation(MoreWorldCrafting.MODID, "fluid_conversion"), FluidConversionRecipeJS.SCHEMA);
        event.register(new ResourceLocation(MoreWorldCrafting.MODID, "fluid_fusion"), FluidFusionRecipeJS.SCHEMA);
        event.register(new ResourceLocation(MoreWorldCrafting.MODID, "lightning_conversion"), LightningConversionRecipeJS.SCHEMA);
        event.register(new ResourceLocation(MoreWorldCrafting.MODID, "void_fusion"), VoidFusionRecipeJS.SCHEMA);
    }
}