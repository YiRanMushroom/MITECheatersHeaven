package com.yiranmushroom.mixin.ite_mixin;

import net.minecraft.GuiContainer;
import net.minecraft.ItemStack;
import net.minecraft.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class GuiContainerMixin {
    @Inject(method = "drawItemStackTooltip(Lnet/minecraft/ItemStack;IILnet/minecraft/Slot;)V", at = @org.spongepowered.asm.mixin.injection.At("HEAD"), cancellable = true)
    private void mod$drawItemStackTooltip(ItemStack par1ItemStack, int par2, int par3, Slot slot, CallbackInfo ci) {

    }
}
