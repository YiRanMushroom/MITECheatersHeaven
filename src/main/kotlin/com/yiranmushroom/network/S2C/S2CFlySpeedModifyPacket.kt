package com.yiranmushroom.network.S2C

import com.yiranmushroom.MITECheatersHeaven
import com.yiranmushroom.mixin_helper.EntityClientPlayerFlySpeedMixinHelper
import moddedmite.rustedironcore.network.Packet
import moddedmite.rustedironcore.network.PacketByteBuf
import net.minecraft.ClientPlayer
import net.minecraft.EntityPlayer
import net.minecraft.ResourceLocation

data class S2CFlySpeedModifyPacket(val isSetIndex: Boolean, val index: Int, val modifier: Float) : Packet {
    constructor(buf: PacketByteBuf) : this(
        buf.readBoolean(),
        buf.readInt(),
        buf.readFloat()
    )

    override fun write(buf: PacketByteBuf) {
        buf.writeBoolean(isSetIndex)
        buf.writeInt(index)
        buf.writeFloat(modifier)
    }

    override fun apply(clientPlayer: EntityPlayer) {
        if (clientPlayer !is ClientPlayer)
            throw IllegalArgumentException("Player has to be a ClientPlayer!")

        if (isSetIndex) {
            EntityClientPlayerFlySpeedMixinHelper.setSpeedIndex(index)
        } else {
            EntityClientPlayerFlySpeedMixinHelper.customMultiplier = modifier
        }
    }

    override fun getChannel(): ResourceLocation {
        return ID
    }

    companion object {
        val ID = ResourceLocation(MITECheatersHeaven.MOD_ID, "modify_fly_speed")
    }
}