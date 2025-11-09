/*
package com.yiranmushroom.network.C2S

import com.yiranmushroom.MITECheatersHeaven
import com.yiranmushroom.enchantments.IDoChaining
import moddedmite.rustedironcore.network.Packet
import moddedmite.rustedironcore.network.PacketByteBuf
import net.minecraft.EntityPlayer
import net.minecraft.ResourceLocation
import net.minecraft.ServerPlayer

class C2SRequestChangeChainingStatePacket : Packet {
    val enableChaining: Boolean

    constructor(buffer: PacketByteBuf) {
        enableChaining = buffer.readBoolean()
    }

    constructor(enableChaining: Boolean) {
        this.enableChaining = enableChaining
    }

    override fun write(buffer: PacketByteBuf) {
        buffer.writeBoolean(enableChaining)
    }

    override fun apply(player: EntityPlayer) {
        if (player !is ServerPlayer) {
            throw IllegalStateException("C2SRequestChangeChainingStatePacket can only be handled on the server side")
        }

        if (player !is IDoChaining) {
            throw IllegalStateException("Player does not implement IDoChaining")
        } else {
            player.requestChaining(enableChaining)
        }
    }

    override fun getChannel(): ResourceLocation {
        return ID
    }

    companion object {
        @JvmStatic
        val ID = ResourceLocation(MITECheatersHeaven.MOD_ID, "c2s_request_change_chaining_state")
    }
}*/
