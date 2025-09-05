package com.xiaoyu.more_world_crafting.recipe;

import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = 
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MoreWorldCrafting.MODID);

    public static final RegistryObject<RecipeSerializer<FluidFusionRecipe>> FLUID_FUSION_SERIALIZER = 
        RECIPE_SERIALIZERS.register("fluid_fusion", FluidFusionRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<FluidConversionRecipe>> FLUID_CONVERSION_SERIALIZER = 
        RECIPE_SERIALIZERS.register("fluid_conversion", FluidConversionRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<VoidConversionRecipe>> VOID_CONVERSION_SERIALIZER = 
        RECIPE_SERIALIZERS.register("void_conversion", VoidConversionRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<FireConversionRecipe>> FIRE_CONVERSION_SERIALIZER = 
        RECIPE_SERIALIZERS.register("fire_conversion", FireConversionRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<LightningConversionRecipe>> LIGHTNING_CONVERSION_SERIALIZER = 
        RECIPE_SERIALIZERS.register("lightning_conversion", LightningConversionRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<ExplosionConversionRecipe>> EXPLOSION_CONVERSION_SERIALIZER = 
        RECIPE_SERIALIZERS.register("explosion_conversion", ExplosionConversionRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<CrushingConversionRecipe>> CRUSHING_CONVERSION_SERIALIZER = 
        RECIPE_SERIALIZERS.register("crushing_conversion", CrushingConversionRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}