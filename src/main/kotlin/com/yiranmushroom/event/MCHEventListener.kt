package com.yiranmushroom.event;

import com.google.common.eventbus.Subscribe;
import com.yiranmushroom.commands.BackCommand
import com.yiranmushroom.commands.DeleteHomeCommand
import com.yiranmushroom.commands.HomeCommand
import com.yiranmushroom.commands.SetHomeCommand
import com.yiranmushroom.enchantments.Enchantments
import com.yiranmushroom.item.Items;
import com.yiranmushroom.scripting.Events;
import net.xiaoyu233.fml.reload.event.CommandRegisterEvent
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

    @Subscribe
    fun onCommandRegister(event: CommandRegisterEvent) {
        event.register(HomeCommand())
        event.register(SetHomeCommand())
        event.register(DeleteHomeCommand())
        event.register(BackCommand())
    }
}
