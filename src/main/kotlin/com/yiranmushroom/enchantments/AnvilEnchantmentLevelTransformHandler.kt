package com.yiranmushroom.enchantments

import moddedmite.rustedironcore.api.event.AbstractHandler

class AnvilEnchantmentLevelTransformHandler : AbstractHandler<IAnvilEnchantmentLevelTransformHandler>(),
    IAnvilEnchantmentLevelTransformHandler {
    override fun transform(enchantmentId: Int, enchantmentLevel: Int): Int {
        return this.listeners.fold(enchantmentLevel) { lastLvl, thisTransform ->
            thisTransform.transform(enchantmentId, lastLvl)
        }
    }
}