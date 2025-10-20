package com.yiranmushroom.event;

import com.google.common.eventbus.Subscribe;
import com.yiranmushroom.enchantments.Enchantments
import com.yiranmushroom.item.Items;
import com.yiranmushroom.scripting.Events;
import net.xiaoyu233.fml.reload.event.EnchantmentRegistryEvent
import net.xiaoyu233.fml.reload.event.ItemRegistryEvent
import net.xiaoyu233.fml.reload.event.RecipeRegistryEvent

// register fish events
class MCHEventListener {
    @Subscribe
    fun onItemRegister(event: ItemRegistryEvent) {
        Items.registerItems(event);
    }
    //please read net.xiaoyu233.fml.reload.event for events

    @Subscribe
    fun onRecipeRegister(event: RecipeRegistryEvent) {
        Events.triggerRecipeRegister(event);
    }

    @Subscribe
    fun onEnchantmentRegister(event: EnchantmentRegistryEvent) {
        Enchantments.enchantmentList.forEach(event::registerEnchantment)
    }
}
