package com.xiaoyu.more_world_crafting.recipe;

import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = 
        DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MoreWorldCrafting.MODID);

    public static final RegistryObject<RecipeType<FluidFusionRecipe>> FLUID_FUSION = 
        RECIPE_TYPES.register("fluid_fusion", () -> new RecipeType<FluidFusionRecipe>() {
            @Override
            public String toString() {
                return "fluid_fusion";
            }
        });

    public static final RegistryObject<RecipeType<FluidConversionRecipe>> FLUID_CONVERSION = 
        RECIPE_TYPES.register("fluid_conversion", () -> new RecipeType<FluidConversionRecipe>() {
            @Override
            public String toString() {
                return "fluid_conversion";
            }
        });

    public static final RegistryObject<RecipeType<VoidConversionRecipe>> VOID_CONVERSION = 
        RECIPE_TYPES.register("void_conversion", () -> new RecipeType<VoidConversionRecipe>() {
            @Override
            public String toString() {
                return "void_conversion";
            }
        });

    public static final RegistryObject<RecipeType<FireConversionRecipe>> FIRE_CONVERSION = 
        RECIPE_TYPES.register("fire_conversion", () -> new RecipeType<FireConversionRecipe>() {
            @Override
            public String toString() {
                return "fire_conversion";
            }
        });

    public static final RegistryObject<RecipeType<LightningConversionRecipe>> LIGHTNING_CONVERSION = 
        RECIPE_TYPES.register("lightning_conversion", () -> new RecipeType<LightningConversionRecipe>() {
            @Override
            public String toString() {
                return "lightning_conversion";
            }
        });

    public static final RegistryObject<RecipeType<ExplosionConversionRecipe>> EXPLOSION_CONVERSION = 
        RECIPE_TYPES.register("explosion_conversion", () -> new RecipeType<ExplosionConversionRecipe>() {
            @Override
            public String toString() {
                return "explosion_conversion";
            }
        });

    public static final RegistryObject<RecipeType<CrushingConversionRecipe>> CRUSHING_CONVERSION = 
        RECIPE_TYPES.register("crushing_conversion", () -> new RecipeType<CrushingConversionRecipe>() {
            @Override
            public String toString() {
                return "crushing_conversion";
            }
        });

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}