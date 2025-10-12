package com.yiranmushroom.mixin;

import com.yiranmushroom.mixin_helper.ClientPlayerScripting;
import net.minecraft.ClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayer.class)
public class ClientPlayerMixin {
    @Inject(method = "getCraftingPeriod", at = @At(value = "RETURN"), cancellable = true)
    private void modify$getCraftingPeriod(float quality_adjusted_crafting_difficulty, CallbackInfoReturnable<Integer> cir) {
        var transform = ClientPlayerScripting.getCraftingPeriodTransform();
        if (transform != null) {
            var result = transform.invoke((ClientPlayer) (Object) this, ((ClientPlayer) (Object) this).openContainer, cir.getReturnValueI());
            cir.setReturnValue(result);
        } // else do nothing
    }
}
