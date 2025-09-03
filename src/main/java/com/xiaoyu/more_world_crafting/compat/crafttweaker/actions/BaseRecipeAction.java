package com.xiaoyu.more_world_crafting.compat.crafttweaker.actions;

import com.blamejared.crafttweaker.api.action.base.IUndoableAction;
import com.xiaoyu.more_world_crafting.MoreWorldCrafting;

public abstract class BaseRecipeAction implements IUndoableAction {
    
    @Override
    public String systemName() {
        return MoreWorldCrafting.MODID;
    }
    
    @Override
    public abstract void apply();
    
    @Override
    public abstract String describe();
    
    @Override
    public abstract void undo();
    
    @Override
    public abstract String describeUndo();
}