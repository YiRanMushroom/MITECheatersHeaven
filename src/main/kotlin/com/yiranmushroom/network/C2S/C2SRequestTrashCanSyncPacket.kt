package com.yiranmushroom.network.C2S

import com.yiranmushroom.MITECheatersHeaven
import moddedmite.rustedironcore.network.Packet
import moddedmite.rustedironcore.network.PacketByteBuf
import net.minecraft.EntityPlayer
import net.minecraft.ResourceLocation
import net.minecraft.ServerPlayer

class C2SRequestTrashCanSyncPacket : Packet {
    override fun write(p0: PacketByteBuf) {

    }

    override fun apply(serverPlayer: EntityPlayer) {
        if (serverPlayer is ServerPlayer) {
            serverPlayer.openContainer.detectAndSendChanges()
        } else {
            throw IllegalArgumentException("This packet can only be handled on the server side by a ServerPlayer.")
        }
    }

    override fun getChannel(): ResourceLocation {
        return ID
    }

    companion object {
        val ID = ResourceLocation(MITECheatersHeaven.MOD_ID, "c2s_request_trash_can_sync")
    }
}