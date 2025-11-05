package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yiranmushroom.mixin_helper.EntityClientPlayerFlySpeedMixinHelper;
import net.minecraft.PlayerCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerCapabilities.class)
public class PlayerCapabilitiesMixin {
    @ModifyExpressionValue(method = "getFlySpeed", at = @At(value = "FIELD", target = "Lnet/minecraft/PlayerCapabilities;flySpeed:F"))
    private float ith$modify$flySpeed(float original) {
        return EntityClientPlayerFlySpeedMixinHelper.modifyFlySpeed(original);
    }
}
