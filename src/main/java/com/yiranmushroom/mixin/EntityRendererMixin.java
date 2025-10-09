package com.yiranmushroom.mixin;

import com.yiranmushroom.mixin_helper.NightVision;
import net.minecraft.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Redirect(method = "updateLightmap", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityClientPlayerMP;isPotionActive(Lnet/minecraft/Potion;)Z"))
    private boolean red$isNightVision(net.minecraft.EntityClientPlayerMP player, net.minecraft.Potion potion) {
        if (potion == net.minecraft.Potion.nightVision) {
            return NightVision.getEnabled() || player.isPotionActive(potion);
        }
        return player.isPotionActive(potion);
    }

    @Inject(method = "getNightVisionBrightness", at = @At("HEAD"), cancellable = true)
    private void inj$getNightVisionBrightness(CallbackInfoReturnable<Float> cir) {
        if (NightVision.getEnabled()) {
            cir.setReturnValue(1.0f);
        }
    }
}
