package com.yiranmushroom.enchantments

import com.yiranmushroom.enchantments.Enchantments.holds
import net.minecraft.*

class FlyingEnchantment(id: Int, difficulty: Int) : Enchantment(id, EnumRarity.epic, difficulty) {
    override fun getNameSuffix(): String {
        return "flying"
    }

    override fun canEnchantItem(item: Item): Boolean {
        return item is ItemCuirass
    }

    override fun isOnCreativeTab(creativeTabs: CreativeTabs): Boolean {
        return creativeTabs == CreativeTabs.tabTools
    }

    override fun getNumLevels(): Int {
        return 1
    }

    companion object {
        @JvmStatic
        fun holdBy(entity: Entity): Boolean {
            return entity holds Enchantments.flyingEnchantment
        }
    }
}