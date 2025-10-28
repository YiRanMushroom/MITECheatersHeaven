package com.yiranmushroom.network.C2S

import com.yiranmushroom.MITECheatersHeaven
import com.yiranmushroom.container.IOpenTrashCan
import com.yiranmushroom.network.S2C.S2COpenTrashCanPacket
import moddedmite.rustedironcore.network.Network
import moddedmite.rustedironcore.network.Packet
import moddedmite.rustedironcore.network.PacketByteBuf
import net.minecraft.EntityPlayer
import net.minecraft.ResourceLocation
import net.minecraft.ServerPlayer

class C2SRequestOpenTrashCanPacket : Packet {
    override fun write(p0: PacketByteBuf) {
        // No data to write
    }

    override fun apply(player: EntityPlayer) {
        if (player is ServerPlayer) {
            if (player is IOpenTrashCan) {
                player.openTrashCan()
            } else {
                throw IllegalStateException("Player does not implement IOpenTrashCan!")
            }
        } else {
            throw IllegalStateException("Player is not a ServerPlayer!")
        }
    }

    override fun getChannel(): ResourceLocation {
        return ID
    }

    companion object {
        val ID = ResourceLocation(MITECheatersHeaven.MOD_ID, "c2s_request_open_trash_can")
    }
}