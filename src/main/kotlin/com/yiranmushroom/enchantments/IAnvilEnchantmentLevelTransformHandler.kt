package com.yiranmushroom.enchantments

@FunctionalInterface
fun interface IAnvilEnchantmentLevelTransformHandler {
    fun transform(enchantmentId: Int, enchantmentLevel: Int) : Int
}