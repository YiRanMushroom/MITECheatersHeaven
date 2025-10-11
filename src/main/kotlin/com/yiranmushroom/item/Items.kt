package com.yiranmushroom.item

import com.yiranmushroom.MITECheatersHeaven
import com.yiranmushroom.scripting.Events
import net.minecraft.Item
import net.minecraft.Material
import net.xiaoyu233.fml.reload.event.ItemRegistryEvent

object Items {
    @JvmStatic
    val lavaInPipes = LavaInPipes(4070, Material.lava).setUnlocalizedName("lava_in_pipes")

    @JvmStatic
    val modManagedItems = mutableMapOf<String, MutableList<Item>>()

    fun <T: Item> ItemRegistryEvent.registerItem(namespace: String, name: String, item: T) : T {
        this.register(namespace, "$namespace:$name", name, item)
        modManagedItems.getOrPut(namespace) { mutableListOf() }.add(item)
        return item
    }

    @JvmStatic
    fun registerItems(event: ItemRegistryEvent) {
        event.registerItem(MITECheatersHeaven.MOD_ID, "lava_in_pipes", lavaInPipes)

        Events.triggerModItemRegister(event)
    }
}