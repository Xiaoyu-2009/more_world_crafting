package com.xiaoyu.more_world_crafting;

import com.xiaoyu.more_world_crafting.config.ModConfig;
import com.xiaoyu.more_world_crafting.recipe.ModRecipeSerializers;
import com.xiaoyu.more_world_crafting.recipe.ModRecipeTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MoreWorldCrafting.MODID)
public class MoreWorldCrafting {
    public static final String MODID = "more_world_crafting";

    public MoreWorldCrafting() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModConfig.register();
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
    }
}