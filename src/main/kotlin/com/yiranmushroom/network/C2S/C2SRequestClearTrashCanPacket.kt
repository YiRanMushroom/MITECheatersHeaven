package com.yiranmushroom.network.C2S

import com.yiranmushroom.MITECheatersHeaven
import com.yiranmushroom.container.IClearTrashCan
import com.yiranmushroom.network.MCHNetwork
import moddedmite.rustedironcore.network.Packet
import moddedmite.rustedironcore.network.PacketByteBuf
import net.minecraft.EntityPlayer
import net.minecraft.ResourceLocation
import net.minecraft.ServerPlayer

class C2SRequestClearTrashCanPacket : Packet {
    override fun write(writeBuffer: PacketByteBuf) {
        // No data to write
    }

    override fun apply(player: EntityPlayer) {
        if (player !is ServerPlayer)
            throw IllegalArgumentException("Player has to be a ServerPlayer!")

        if (player is IClearTrashCan)
            player.clearTrashCan()
        else
            throw IllegalStateException("Player does not implement IClearTrashCan!")
    }

    override fun getChannel(): ResourceLocation {
        return MCHNetwork.Channel
    }

    companion object {
        val ID = ResourceLocation(MITECheatersHeaven.MOD_ID, "c2s_request_clear_trash_can")
    }
}