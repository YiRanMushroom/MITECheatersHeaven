package com.yiranmushroom.event;

import com.google.common.eventbus.Subscribe;
import com.yiranmushroom.item.Items;
import com.yiranmushroom.scripting.Events;
import net.xiaoyu233.fml.reload.event.ItemRegistryEvent;

// register fish events
public class FishEventListen {
    @Subscribe
    public void onItemRegister(ItemRegistryEvent event) {
        Items.registerItems(event);
    }
    //please read net.xiaoyu233.fml.reload.event for events

    @Subscribe
    public void onRecipeRegister(net.xiaoyu233.fml.reload.event.RecipeRegistryEvent event) {
        Events.triggerRecipeRegister(event);
    }
}
