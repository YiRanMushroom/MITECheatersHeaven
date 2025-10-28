package com.yiranmushroom.network.S2C

import com.yiranmushroom.container.IOpenTrashCan
import com.yiranmushroom.network.MCHNetwork
import moddedmite.rustedironcore.network.Packet
import moddedmite.rustedironcore.network.PacketByteBuf
import net.minecraft.ClientPlayer
import net.minecraft.EntityPlayer
import net.minecraft.ResourceLocation


class S2COpenTrashCanPacket : Packet {
    override fun write(p0: PacketByteBuf) {
        // No data to write
    }

    override fun apply(player: EntityPlayer) {
        if (player is ClientPlayer) {
            if (player is IOpenTrashCan) {
                player.openTrashCan()
            } else {
                throw IllegalStateException("Player does not implement IOpenTrashCan!")
            }
        } else {
            throw IllegalStateException("Player does not implement IOpenTrashCan!")
        }
    }

    override fun getChannel(): ResourceLocation {
        return MCHNetwork.Channel
    }

    companion object {
        val ID = ResourceLocation("mitecheatersheaven", "s2c_open_trash_can")
    }
}