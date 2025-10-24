package com.yiranmushroom.enchantments

import com.yiranmushroom.utils.GetAllStaticMembersFromClassDerivedFrom
import net.minecraft.Enchantment
import net.minecraft.EnchantmentHelper
import net.minecraft.Entity

object Enchantments {
    val flyingEnchantment = FlyingEnchantment(130, 10)

    val enchantmentList by lazy {
        GetAllStaticMembersFromClassDerivedFrom<Enchantments, Enchantment>()
    }

    infix fun Entity.holds(enchantment: Enchantment): Boolean {
        return EnchantmentHelper.getEnchantmentLevels(
            enchantment, this.lastActiveItems
        ).let { it > 0 }
    }

    @JvmStatic
    val anvilEnchantmentLevelTransformHandler = AnvilEnchantmentLevelTransformHandler()
}