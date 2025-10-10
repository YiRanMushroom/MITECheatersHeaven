package com.yiranmushroom.item

import com.google.common.eventbus.Subscribe
import com.yiranmushroom.MITECheatersHeaven
import net.minecraft.Item
import net.minecraft.Material
import net.xiaoyu233.fml.api.INamespaced
import net.xiaoyu233.fml.reload.event.ItemRegistryEvent

object Items {
    @JvmStatic
    val lavaInPipes = LavaInPipes(4070, Material.lava).setUnlocalizedName("lava_in_pipes")

    fun ItemRegistryEvent.registerItem(namespace : String, name: String, item: Item) {
        this.register(namespace, "$namespace:$name", name, item);
    }

    @JvmStatic
    fun registerItems(event: ItemRegistryEvent) {
        event.registerItem(MITECheatersHeaven.MOD_ID, "lava_in_pipes", lavaInPipes)
    }


}