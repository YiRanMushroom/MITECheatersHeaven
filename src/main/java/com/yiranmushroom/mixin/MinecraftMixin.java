package com.yiranmushroom.mixin;

import com.yiranmushroom.scripting.Events;
import jdk.jfr.Event;
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
}
