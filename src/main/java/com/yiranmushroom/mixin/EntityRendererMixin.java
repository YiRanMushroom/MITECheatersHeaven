package com.yiranmushroom.mixin;

import com.yiranmushroom.mixin_helper.NightVision;
import net.minecraft.EntityRenderer;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Redirect(method = "updateLightmap", at = @At(value = "FIELD", target = "Lnet/minecraft/Minecraft;night_vision_override:Z"))
    private boolean red$night_vision_override() {
        return NightVision.getEnabled() || Minecraft.night_vision_override;
    }

    @Inject(method = "getNightVisionBrightness", at = @At("HEAD"), cancellable = true)
    private void inj$getNightVisionBrightness(CallbackInfoReturnable<Float> cir) {
        if (NightVision.getEnabled()) {
            cir.setReturnValue(1.0f);
        }
    }
}
