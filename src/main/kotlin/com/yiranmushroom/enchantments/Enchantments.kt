package com.yiranmushroom.enchantments

import com.yiranmushroom.utils.GetAllStaticMembersFromClassDerivedFrom
import net.minecraft.Enchantment
import net.minecraft.EnchantmentHelper
import net.minecraft.Entity
import net.xiaoyu233.fml.reload.utils.IdUtil

object Enchantments {
    @JvmField
    val flyingEnchantment = FlyingEnchantment(IdUtil.getNextEnchantmentID(), 10)

    @JvmField
    val oreSmeltingEnchantment = OreSmeltingEnchantment(IdUtil.getNextEnchantmentID(), 10)

    @JvmField
    val chainingEnchant = ChainingEnchantment(IdUtil.getNextEnchantmentID(), 10)

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