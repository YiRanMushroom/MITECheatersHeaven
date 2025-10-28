package com.yiranmushroom.network

import com.yiranmushroom.MITECheatersHeaven
import com.yiranmushroom.network.C2S.C2SRequestClearTrashCanPacket
import com.yiranmushroom.network.S2C.S2COpenTrashCanPacket
import moddedmite.rustedironcore.network.PacketReader
import net.minecraft.ResourceLocation
import net.xiaoyu233.fml.FishModLoader

object MCHNetwork {
    val Channel = ResourceLocation(MITECheatersHeaven.MOD_ID, "network_channel")

    fun registerClientPacketListeners() {
        PacketReader.registerClientPacketReader(S2COpenTrashCanPacket.ID) { buffer -> S2COpenTrashCanPacket() }
    }

    fun registerServerPacketListeners() {
        PacketReader.registerServerPacketReader(C2SRequestClearTrashCanPacket.ID) { buffer -> C2SRequestClearTrashCanPacket() }
    }

    fun init() {
        if (!FishModLoader.isServer()) {
            registerClientPacketListeners()
        }

        registerServerPacketListeners()
    }
}