package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.yiranmushroom.config.MITECheatersHeavenConfig;
import moddedmite.rustedironcore.RustedIronCore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RustedIronCore.class)
public class RustedIronCoreMixin {
    @ModifyReturnValue(method = "shouldRenderStatement", at = @At("RETURN"), require = 0)
    private static boolean mod$shouldRenderStatement(boolean original) {
        return original && !MITECheatersHeavenConfig.getTurnOffRustyIronCoreStatementConfig().getBooleanValue();
    }
}
