package com.yiranmushroom.container

import com.yiranmushroom.network.C2S.C2SRequestClearTrashCanPacket
import moddedmite.rustedironcore.network.Network
import net.minecraft.EntityPlayer
import net.minecraft.GuiChest
import net.minecraft.IInventory

class GuiTrashCan(player: EntityPlayer, par2IInventory: IInventory) : GuiChest(player, par2IInventory) {
    override fun onGuiClosed() {
        super.onGuiClosed()
        Network.sendToServer(C2SRequestClearTrashCanPacket())
    }
}