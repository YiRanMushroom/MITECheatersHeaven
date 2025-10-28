package com.yiranmushroom.network.S2C

import com.yiranmushroom.MITECheatersHeaven
import com.yiranmushroom.container.IGetTrashCanInventory
import moddedmite.rustedironcore.network.Packet
import moddedmite.rustedironcore.network.PacketByteBuf
import net.minecraft.ClientPlayer
import net.minecraft.EntityPlayer
import net.minecraft.ResourceLocation

class S2CTrashCanClearedPacket : Packet {
    override fun write(p0: PacketByteBuf) {

    }

    override fun apply(clientPlayer: EntityPlayer?) {
        if (clientPlayer is ClientPlayer) {
            (clientPlayer as IGetTrashCanInventory).getTrashCanInventory().destroyInventory()
        } else {
            throw IllegalArgumentException("Player has to be a ClientPlayer!")
        }
    }

    override fun getChannel(): ResourceLocation {
        return ID
    }

    companion object {
        val ID = ResourceLocation(MITECheatersHeaven.MOD_ID, "s2c_trash_can_cleared")
    }
}