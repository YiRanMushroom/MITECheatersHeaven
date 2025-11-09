package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.yiranmushroom.MITECheatersHeaven;
import com.yiranmushroom.api.IIsChaining;
import com.yiranmushroom.config.MITECheatersHeavenConfig;
import net.minecraft.Packet;
import net.minecraft.Packet85SimpleSignal;
import net.minecraft.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMpAddChainingMixin {
    @WrapOperation(method = "sendDiggingPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/PlayerControllerMP;sendPacket(Lnet/minecraft/Packet;)V"))
    // second one
    private void ith$warpAddChaining(PlayerControllerMP instance, Packet packet, Operation<Void> original) {
        if (!(packet instanceof Packet85SimpleSignal)) {
            MITECheatersHeaven.getLOGGER().error("Expected Packet85SimpleSignal, but got: {}", packet.getClass().getName());
            original.call(instance, packet);
            return;
        } else if (packet instanceof IIsChaining isChainingPacket) {
            isChainingPacket.setChaining(MITECheatersHeavenConfig.getEnableChainingHotkey().pressed);
            original.call(instance, packet);
            return;
        } else {
            MITECheatersHeaven.getLOGGER().error("Packet85SimpleSignal does not implement IIsChaining!");
        }

        original.call(instance, packet);
    }
}
