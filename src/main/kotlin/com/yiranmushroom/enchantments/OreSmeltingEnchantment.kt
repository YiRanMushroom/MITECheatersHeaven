package com.yiranmushroom.enchantments

import net.minecraft.*


class OreSmeltingEnchantment(id: Int, difficulty: Int) : Enchantment(id, EnumRarity.epic, difficulty) {
    override fun getNameSuffix(): String {
        return "ore_smelting"
    }

    override fun canEnchantItem(item: Item): Boolean {
        return item is ItemPickaxe // war hammer extends pickaxe
    }

    override fun isOnCreativeTab(creativeTabs: CreativeTabs): Boolean {
        return creativeTabs == CreativeTabs.tabTools
    }

    override fun getNumLevels(): Int {
        return 3
    }

    companion object {
        @JvmStatic
        fun EntityLivingBase.getHoldingOreSmeltingEnchantmentLevel(): Int {
            return EnchantmentHelper.getEnchantmentLevel(
                Enchantments.oreSmeltingEnchantment.effectId,
                this.heldItemStack
            )
        }
    }
}