package com.yiranmushroom.mixin.ite_mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.EnumQuality;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = EnumQuality.class, priority = 9999)
@Restriction(require = @Condition(value = "mite_ite"))
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
