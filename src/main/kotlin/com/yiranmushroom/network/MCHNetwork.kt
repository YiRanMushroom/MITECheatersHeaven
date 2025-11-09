package com.yiranmushroom.network

import com.yiranmushroom.network.C2S.C2SRequestChangeChainingStatePacket
import com.yiranmushroom.network.C2S.C2SRequestClearTrashCanPacket
import com.yiranmushroom.network.C2S.C2SRequestOpenTrashCanPacket
import com.yiranmushroom.network.C2S.C2SRequestTrashCanSyncPacket
import com.yiranmushroom.network.S2C.S2CFlySpeedModifyPacket
import com.yiranmushroom.network.S2C.S2COpenTrashCanPacket
import com.yiranmushroom.network.S2C.S2CTrashCanClearedPacket
import moddedmite.rustedironcore.network.PacketReader
import net.xiaoyu233.fml.FishModLoader

object MCHNetwork {
    fun registerClientPacketListeners() {
        PacketReader.registerClientPacketReader(S2COpenTrashCanPacket.ID) { buffer ->
            S2COpenTrashCanPacket()
        }
        PacketReader.registerClientPacketReader(S2CTrashCanClearedPacket.ID) { buffer ->
            S2CTrashCanClearedPacket()
        }
        PacketReader.registerClientPacketReader(S2CFlySpeedModifyPacket.ID, ::S2CFlySpeedModifyPacket)
    }

    fun registerServerPacketListeners() {
        PacketReader.registerServerPacketReader(C2SRequestClearTrashCanPacket.ID) { buffer ->
            C2SRequestClearTrashCanPacket()
        }
        PacketReader.registerServerPacketReader(C2SRequestOpenTrashCanPacket.ID) { buffer ->
            C2SRequestOpenTrashCanPacket()
        }
        PacketReader.registerServerPacketReader(C2SRequestTrashCanSyncPacket.ID) { buffer ->
            C2SRequestTrashCanSyncPacket()
        }
        PacketReader.registerServerPacketReader(
            C2SRequestChangeChainingStatePacket.ID,
            ::C2SRequestChangeChainingStatePacket
        )
    }

    fun init() {
        if (!FishModLoader.isServer()) {
            registerClientPacketListeners()
        }

        registerServerPacketListeners()
    }
}