package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.World;
import net.minecraft.WorldGenMinable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WorldGenMinable.class, priority = 998)
public class WorldGenMinableMixin {
    @Inject(method = "getMinVeinHeight", at = @At("HEAD"), cancellable = true)
    private void getMinVeinHeight(CallbackInfoReturnable<Integer> cir, @Local(argsOnly = true, ordinal = 0) World world) {
        if (world.isUnderworld()) {
            cir.setReturnValue(0);
        }
    }
}
