package com.yiranmushroom.mixin;

import com.yiranmushroom.mixin_helper.CraftingResultScripting;
import net.minecraft.CraftingResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingResult.class)
public class CraftingResultMixin {
    @Inject(method = "getQualityAdjustedDifficulty(Lnet/minecraft/EnumQuality;)F", at = @At("RETURN"), cancellable = true)
    void inj$getQualityAdjustedDifficulty(net.minecraft.EnumQuality quality, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(CraftingResultScripting.getGetQualityAdjustedDifficultyModify().invoke((CraftingResult) (Object) this, quality, cir.getReturnValueF()));
    }
}
