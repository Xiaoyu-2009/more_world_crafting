package com.xiaoyu.more_world_crafting.crafting;

import com.xiaoyu.more_world_crafting.config.ModConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class CraftingAPI {
    public static ItemEntity createOutputItem(ServerLevel level, Vec3 position, ItemStack result, ItemEntity originalItem) {
        ItemEntity resultEntity = new ItemEntity(level, position.x, position.y, position.z, result);
        applyOutputEffects(resultEntity, originalItem);
        return resultEntity;
    }

    public static ItemEntity createOutputItemWithJump(ServerLevel level, Vec3 position, ItemStack result, ItemEntity originalItem, double jumpHeight) {
        ItemEntity resultEntity = createOutputItem(level, position, result, originalItem);
        if (ModConfig.OUTPUT_ITEM_SPAWN_JUMP_ENABLED.get()) {
            resultEntity.setDeltaMovement(0, jumpHeight, 0);
        }
        return resultEntity;
    }

    public static void applyOutputEffects(ItemEntity resultEntity, ItemEntity originalItem) {
        resultEntity.setInvulnerable(true);

        if (originalItem != null && originalItem.getOwner() != null) {
            resultEntity.setThrower(originalItem.getOwner().getUUID());
        }

        resultEntity.setDefaultPickUpDelay();
        
        if (ModConfig.OUTPUT_ITEM_GLOW_ENABLED.get()) {
            resultEntity.setGlowingTag(true);
        }
        
        if (ModConfig.OUTPUT_ITEM_NAME_DISPLAY_ENABLED.get()) {
            resultEntity.setCustomName(resultEntity.getItem().getHoverName());
            resultEntity.setCustomNameVisible(true);
        }
    }

    public static void protectFluidFusionInput(ItemEntity inputItem) {
        if (ModConfig.FLUID_FUSION_INVULNERABLE_ENABLED.get()) {
            inputItem.setInvulnerable(true);
        }
    }
}