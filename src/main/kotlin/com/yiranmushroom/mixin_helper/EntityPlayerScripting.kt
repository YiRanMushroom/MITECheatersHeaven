package com.yiranmushroom.mixin_helper

import net.minecraft.Entity
import net.minecraft.EntityPlayer
import net.minecraft.EnumEntityReachContext
import net.minecraft.Item
import net.minecraft.EnumQuality
import net.minecraft.Block

object EntityPlayerScripting {
    @JvmStatic
    var getReachModify1: EntityPlayer.(EnumEntityReachContext, Entity, Float) -> Float =
        { c: EnumEntityReachContext, e: Entity, original: Float -> original }

    @JvmStatic
    var getReachModify2: EntityPlayer.(Block, Int, Float) -> Float =
        { b: Block, m: Int, original: Float -> original }

    @JvmStatic
    var getRelativeBlockHardnessModify: EntityPlayer.(Int, Int, Int, Boolean, Float) -> Float =
        { x: Int, y: Int, z: Int, isRemote: Boolean, original: Float -> original }

    @JvmStatic
    var getMaxCraftingQualityModify: EntityPlayer.(Float, Item, IntArray?, EnumQuality) -> EnumQuality =
        { difficulty: Float, item: Item, _: IntArray?, original: EnumQuality -> original }

    @JvmStatic
    var getMinCraftingQualityModify: EntityPlayer.(Item, IntArray?, EnumQuality) -> EnumQuality =
        { item: Item, _: IntArray?, original: EnumQuality -> original }
}