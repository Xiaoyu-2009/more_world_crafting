package com.xiaoyu.more_world_crafting.crafting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.resources.ResourceLocation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;
import com.xiaoyu.more_world_crafting.recipe.ExplosionConversionRecipe;
import com.xiaoyu.more_world_crafting.recipe.ModRecipeTypes;

@Mod.EventBusSubscriber(modid = MoreWorldCrafting.MODID)
public class ExplosionConversionManager {
    private static final Map<UUID, ConversionData> pendingConversions = new HashMap<>();
    private static final Map<ResourceLocation, ExplosionConversionRecipe> customRecipes = new HashMap<>();
    private static final double EXPLOSION_JUMP_HEIGHT = 0.6;
    private static final int CONVERSION_DELAY = 10;
    private static final double EXPLOSION_RADIUS = 8.0;
    
    public static void tick(Level level) {
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        processConversions(serverLevel);
    }

    private static void processConversions(ServerLevel level) {
        Iterator<Map.Entry<UUID, ConversionData>> iterator = pendingConversions.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<UUID, ConversionData> entry = iterator.next();
            ConversionData data = entry.getValue();
            
            data.tickCount++;
            
            if (data.tickCount >= CONVERSION_DELAY) {
                ItemEntity targetItem = null;
                for (var entity : level.getAllEntities()) {
                    if (entity instanceof ItemEntity item && item.getUUID().equals(data.itemId) && !item.isRemoved()) {
                        targetItem = item;
                        break;
                    }
                }
                
                if (targetItem != null) {
                    boolean conversionSuccess = performConversion(level, data.originalItemStack, data.recipe, data.targetPlayer, data.position);
                    if (conversionSuccess) {
                        targetItem.discard();
                    } else {
                        targetItem.setInvulnerable(false);
                    }
                } else {
                    performConversion(level, data.originalItemStack, data.recipe, data.targetPlayer, data.position);
                }

                iterator.remove();
            }
        }
    }
    
    private static class ConversionData {
        UUID itemId;
        Player targetPlayer;
        ExplosionConversionRecipe recipe;
        ItemStack originalItemStack;
        Vec3 position;
        int tickCount;
        
        ConversionData(UUID itemId, Player targetPlayer, ExplosionConversionRecipe recipe, ItemStack originalItemStack, Vec3 position) {
            this.itemId = itemId;
            this.targetPlayer = targetPlayer;
            this.recipe = recipe;
            this.originalItemStack = originalItemStack;
            this.position = position;
            this.tickCount = 0;
        }
    }

    public static void preHandleExplosion(ServerLevel level, Explosion explosion) {
        if (level.isClientSide()) return;

        Vec3 explosionPos = explosion.getPosition();
        
        AABB searchArea = new AABB(
            explosionPos.x - EXPLOSION_RADIUS, explosionPos.y - EXPLOSION_RADIUS, explosionPos.z - EXPLOSION_RADIUS,
            explosionPos.x + EXPLOSION_RADIUS, explosionPos.y + EXPLOSION_RADIUS, explosionPos.z + EXPLOSION_RADIUS
        );
        
        List<ItemEntity> itemsInArea = level.getEntitiesOfClass(ItemEntity.class, searchArea);
        
        for (ItemEntity itemEntity : itemsInArea) {
            if (itemEntity.isRemoved() || itemEntity.getItem().isEmpty()) continue;
            
            ExplosionConversionRecipe recipe = findMatchingRecipe(level, itemEntity.getItem());
            if (recipe != null) {
                UUID itemId = itemEntity.getUUID();
                if (!pendingConversions.containsKey(itemId)) {
                    Player targetPlayer = getItemThrower(itemEntity);
                    pendingConversions.put(itemId, new ConversionData(itemId, targetPlayer, recipe, itemEntity.getItem().copy(), itemEntity.position()));
                    itemEntity.setInvulnerable(true);
                }
            }
        }
    }

    private static boolean performConversion(ServerLevel level, ItemStack originalStack, ExplosionConversionRecipe recipe, Player targetPlayer, Vec3 position) {
        int inputCount = originalStack.getCount();
        
        if (inputCount <= 0) {
            return false;
        }

        if (level.random.nextFloat() > recipe.getConversionChance()) {
            return false;
        }
        
        ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
        if (result.isEmpty()) {
            return false;
        }

        int resultPerInput = result.getCount();
        int totalResultCount = Math.max(1, inputCount * resultPerInput);
        result.setCount(totalResultCount);

        ItemEntity resultEntity = CraftingAPI.createOutputItemWithJump(level, position, result, null, EXPLOSION_JUMP_HEIGHT);
        
        return level.addFreshEntity(resultEntity);
    }

    private static ExplosionConversionRecipe findMatchingRecipe(ServerLevel level, ItemStack itemStack) {
        List<ExplosionConversionRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.EXPLOSION_CONVERSION.get());
        
        List<ExplosionConversionRecipe> allRecipes = new java.util.ArrayList<>(recipes);
        allRecipes.addAll(customRecipes.values());
        
        for (ExplosionConversionRecipe recipe : allRecipes) {
            if (recipe.getIngredient().test(itemStack)) {
                return recipe;
            }
        }
        
        return null;
    }

    private static Player getItemThrower(ItemEntity item) {
        if (item.getOwner() instanceof Player player) {
            return player;
        }
        return null;
    }
    
    @SubscribeEvent
    public static void onExplosionStart(ExplosionEvent.Start event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            preHandleExplosion(serverLevel, event.getExplosion());
        }
    }
    
    public static void addCustomRecipe(ExplosionConversionRecipe recipe) {
        customRecipes.put(recipe.getId(), recipe);
    }
    
    public static void removeCustomRecipe(ResourceLocation recipeId) {
        customRecipes.remove(recipeId);
    }
    
    public static java.util.Collection<ExplosionConversionRecipe> getCustomRecipes() {
        return customRecipes.values();
    }
}