package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yiranmushroom.mixin_helper.NightVision;
import net.minecraft.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @ModifyExpressionValue(method = "updateLightmap", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityClientPlayerMP;isPotionActive(Lnet/minecraft/Potion;)Z"))
    private boolean mod$isPotionActive(boolean original) {
        return original || NightVision.getEnabled();
    }

    @ModifyExpressionValue(method = "updateLightmap", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityRenderer;getNightVisionBrightness(Lnet/minecraft/EntityPlayer;F)F"))
    private float mod$getNightVisionBrightness(float original) {
        return NightVision.getEnabled() ? 1.0f : original;
    }
}
