package com.yiranmushroom.container

import com.yiranmushroom.MITECheatersHeaven.Companion.LOGGER
import net.minecraft.EntityPlayer
import net.minecraft.InventoryBasic
import net.minecraft.ItemStack
import net.minecraft.Minecraft
import net.xiaoyu233.fml.FishModLoader

class InventoryTrashCan : InventoryBasic("Trash Can", true, 27) {
    override fun closeChest() {
        super.closeChest()
    }

    override fun isItemValidForSlot(par1: Int, par2ItemStack: ItemStack?): Boolean {
        return true
    }

    override fun openChest() {
        super.openChest()
    }

    override fun isUseableByPlayer(par1EntityPlayer: EntityPlayer): Boolean {
        return true
    }

    override fun onInventoryChanged() {
        super.onInventoryChanged()
    }

    override fun setInventorySlotContents(par1: Int, par2ItemStack: ItemStack?) {
        LOGGER.info("Set slot $par1 to $par2ItemStack")
        super.setInventorySlotContents(par1, par2ItemStack)
    }
}