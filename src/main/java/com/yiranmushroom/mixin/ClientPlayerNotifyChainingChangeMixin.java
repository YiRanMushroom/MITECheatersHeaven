package com.yiranmushroom.mixin;

import com.yiranmushroom.config.MITECheatersHeavenConfig;
import com.yiranmushroom.enchantments.IDoChaining;
import com.yiranmushroom.network.C2S.C2SRequestChangeChainingStatePacket;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.ClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayer.class)
public class ClientPlayerNotifyChainingChangeMixin implements IDoChaining {
    @Unique
    private boolean ith$chainingButtonState = false;

    @Inject(method = "onLivingUpdate", at = @At("RETURN"))
    private void ith$notifyChainingChange(CallbackInfo ci) {
        boolean keyState = MITECheatersHeavenConfig.getEnableChainingHotkey().pressed;
        if (keyState != ith$chainingButtonState) {
            ith$chainingButtonState = keyState;
            this.requestChaining(keyState);
        }
    }

    @Override
    public boolean doChaining() {
        throw new UnsupportedOperationException("ClientPlayerNotifyChainingChangeMixin#doChaining should not be called!");
    }

    @Override
    public void requestChaining(boolean on) {
        Network.sendToServer(new C2SRequestChangeChainingStatePacket(on));
    }
}
