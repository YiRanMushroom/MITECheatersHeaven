package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.yiranmushroom.config.MITECheatersHeavenConfig;
import com.yiranmushroom.scripting.Events;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void inj$MinecraftConstructor$PostInitialization(CallbackInfo ci) {
        Events.triggerPostInitialization();
    }

    @ModifyReturnValue(method = "inDevMode", at = @At("RETURN"))
    private static boolean mod$inDevMode(boolean original){
        return original || MITECheatersHeavenConfig.getEnableDevModePrivilegeValue();
    }
}
