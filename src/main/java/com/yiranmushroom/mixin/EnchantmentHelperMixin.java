package com.yiranmushroom.mixin;

import net.minecraft.EnchantmentHelper;
import net.minecraft.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "getEnchantmentLevelsAlteredByItemEnchantability", at = @At("RETURN"), cancellable = true)
    private static void onGetEnchantmentLevelsAlteredByItemEnchantability(int enchantment_levels, Item item, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Math.max(enchantment_levels, cir.getReturnValueI()));
    }
}
