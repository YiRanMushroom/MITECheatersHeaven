package com.yiranmushroom.mixin;

import com.yiranmushroom.mixin_helper.BiomeDecoratorScripting;
import net.minecraft.BiomeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeDecorator.class)
public class BiomeDecoratorMixin {
    @Inject(method = "generateOres", at = @At("RETURN"))
    private void generateOresInject(CallbackInfo ci) {
        if (BiomeDecoratorScripting.getCustomDecorationCallback() != null) {
//            MITECheatersHeaven.getLOGGER().info("Custom decoration callback has been called");
            BiomeDecoratorScripting.getCustomDecorationCallback().invoke((BiomeDecorator) (Object) this);
//            ci.cancel();
        }
    }
}
