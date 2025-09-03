package com.xiaoyu.more_world_crafting.compat.crafttweaker;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.managers.FireFusionManager;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.managers.FluidFusionManager;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.managers.VoidFusionManager;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.managers.FluidConversionManager;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.managers.LightningConversionManager;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.managers.ExplosionConversionManager;
import com.xiaoyu.more_world_crafting.compat.crafttweaker.managers.CrushingConversionManager;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.more_world_crafting.MoreWorldCrafting")
public class MoreWorldCraftingCrT {
    
    @ZenCodeType.Field
    public static final FireFusionManager fireFusion = new FireFusionManager();
    
    @ZenCodeType.Field  
    public static final FluidFusionManager fluidFusion = new FluidFusionManager();
    
    @ZenCodeType.Field
    public static final VoidFusionManager voidFusion = new VoidFusionManager();
    
    @ZenCodeType.Field
    public static final FluidConversionManager fluidConversion = new FluidConversionManager();
    
    @ZenCodeType.Field
    public static final LightningConversionManager lightningConversion = new LightningConversionManager();
    
    @ZenCodeType.Field
    public static final ExplosionConversionManager explosionConversion = new ExplosionConversionManager();
    
    @ZenCodeType.Field
    public static final CrushingConversionManager crushingConversion = new CrushingConversionManager();
}