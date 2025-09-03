package com.xiaoyu.more_world_crafting.compat.jei.api;

import mezz.jei.api.gui.drawable.IDrawable;

import java.util.List;

public interface IMultiIconProvider {
    List<IDrawable> getIcons();
    IDrawable getCurrentIcon(long tickCount);
    boolean isAlternating();
    int getAlternatingInterval();
}