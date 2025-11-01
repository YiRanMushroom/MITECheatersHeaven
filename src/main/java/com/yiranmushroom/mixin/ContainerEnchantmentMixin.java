package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.yiranmushroom.mixin_helper.EnchantmentScripting;
import net.minecraft.ContainerEnchantment;
import net.minecraft.EnchantmentHelper;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(ContainerEnchantment.class)
public class ContainerEnchantmentMixin {
    @Redirect(method = "enchantItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/EnchantmentHelper;buildEnchantmentList(Ljava/util/Random;Lnet/minecraft/ItemStack;I)Ljava/util/List;"))
    private List mod$buildEnchantmentList(Random random, ItemStack itemStack, int enchantmentLevel) {
        return EnchantmentHelper.buildEnchantmentList(random, itemStack, EnchantmentScripting.getTransformEnchantmentLevel().invoke(random, itemStack, enchantmentLevel));
    }

    @Inject(method = "enchantItem", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/IInventory;getStackInSlot(I)Lnet/minecraft/ItemStack;", ordinal = 0))
    private void onEnchantItem(EntityPlayer par1EntityPlayer, int par2,
                               CallbackInfoReturnable<Boolean> cir, @Local(name = "var3") ItemStack var3) {
        var3.clearEnchantTagList();
    }

}
