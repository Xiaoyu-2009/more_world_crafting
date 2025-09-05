package com.xiaoyu.more_world_crafting.event;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.crafting.CrushingConversionManager;
import com.xiaoyu.more_world_crafting.crafting.ExplosionConversionManager;
import com.xiaoyu.more_world_crafting.crafting.FireFusionManager;
import com.xiaoyu.more_world_crafting.crafting.FluidConversionManager;
import com.xiaoyu.more_world_crafting.crafting.FluidFusionManager;
import com.xiaoyu.more_world_crafting.crafting.LightningConversionManager;
import com.xiaoyu.more_world_crafting.crafting.VoidFusionManager;
import net.minecraft.world.entity.LightningBolt;

@Mod.EventBusSubscriber(modid = MoreWorldCrafting.MODID)
public class CraftingEventHandler {
    
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        event.getServer().getAllLevels().forEach(level -> {
            FluidFusionManager.tick(level);
            FluidConversionManager.tick(level);
            VoidFusionManager.tick(level);
            FireFusionManager.tick(level);
            LightningConversionManager.tick(level);
            ExplosionConversionManager.tick(level);
            CrushingConversionManager.tick(level);
        });
    }
    
    @SubscribeEvent
    public static void onLightningJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LightningBolt lightning) {
            LightningConversionManager.handleLightningStrike(lightning);
        }
    }
}