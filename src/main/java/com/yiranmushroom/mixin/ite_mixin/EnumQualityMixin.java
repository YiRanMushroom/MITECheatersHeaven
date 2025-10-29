package com.yiranmushroom.mixin.ite_mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.EnumQuality;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EnumQuality.class, priority = 9999)
public class EnumQualityMixin {
    @Shadow
    @Final
    private float durability_modifier;

    @ModifyReturnValue(method = "getDurabilityModifier", at = @At("TAIL"))
    private float getDurabilityModifier(float original) {
//        System.out.println("EnumQualityMixin getDurabilityModifier called");
        return this.durability_modifier;
    }
}
