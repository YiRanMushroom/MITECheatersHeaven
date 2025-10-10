package com.yiranmushroom.mixin;

import com.yiranmushroom.mixin_helper.EnchantmentScripting;
import com.yiranmushroom.scripting.ScriptingEngine;
import net.minecraft.ContainerEnchantment;
import net.minecraft.EnchantmentHelper;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Random;

@Mixin(ContainerEnchantment.class)
public class ContainerEnchantmentMixin {
    @Redirect(method = "enchantItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/EnchantmentHelper;buildEnchantmentList(Ljava/util/Random;Lnet/minecraft/ItemStack;I)Ljava/util/List;"))
    private List mod$buildEnchantmentList(Random random, ItemStack itemStack, int enchantmentLevel) {
        return EnchantmentHelper.buildEnchantmentList(random, itemStack, EnchantmentScripting.getTransformEnchantmentLevel().invoke(random, itemStack, enchantmentLevel));
    }
}
