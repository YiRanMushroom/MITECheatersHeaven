package com.yiranmushroom.ric_handlers

import moddedmite.rustedironcore.api.event.Handlers
import moddedmite.rustedironcore.api.event.handler.GravelDropHandler
import moddedmite.rustedironcore.api.event.listener.IGravelDropListener
import moddedmite.rustedironcore.api.event.listener.IPlayerAttributeListener
import net.minecraft.BlockBreakInfo
import net.minecraft.EntityPlayer
import net.minecraft.Item
import kotlin.math.max

class ITHGravelDropHandler : IGravelDropListener {
    override fun onDropAsGravelChanceModify(info: BlockBreakInfo, original: Float): Float {
        return original / 2
    }

    override fun onDropFlintAsChipChanceModify(info: BlockBreakInfo, original: Float): Float {
        return 0.0f
    }

    override fun onDropAsFlintChanceModify(info: BlockBreakInfo, original: Float): Float {
        return original / 1.5f
    }

    companion object {
        var changed = false
    }

    init {
        if (!changed) {
            Handlers.GravelDrop.apply {
                this.unregisterGravelLootEntry(GravelDropHandler.DiamondEntry)
                this.unregisterGravelLootEntry(GravelDropHandler.EmeraldEntry)
                this.registerGravelLootEntry(
                    GravelDropHandler.GravelLootEntry(
                        GravelDropHandler.DiamondEntry.weight() * 16,
                        {
                            Item.diamond.itemID
                        }
                    )
                )

                this.registerGravelLootEntry(
                    GravelDropHandler.GravelLootEntry(
                        GravelDropHandler.EmeraldEntry.weight() * 16,
                        {
                            Item.emerald.itemID
                        }
                    )
                )
            }
        } else {
            changed = true
        }
    }
}

class ITHPlayerAttributeHandler : IPlayerAttributeListener {
    override fun onHealthLimitModify(player: EntityPlayer, original: Float): Float {
        val maxVal = (6 + player.experienceLevel / 5 * 2.0).coerceAtMost(200.0).coerceAtLeast(6.0)
        return max(original, maxVal.toFloat())
    }

    override fun onLevelLimitModify(original: Int): Int {
        return max(original, 1000)
    }

    override fun onNutritionLimitModify(player: EntityPlayer, original: Int): Int {
        return original * 4
    }
}