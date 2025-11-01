package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @ModifyExpressionValue(method = "isEnchantable", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;isItemEnchanted()Z"))
    private boolean modifyIsEnchantable(boolean original) {
        if (this.getItem() == Item.enchantedBook) {
            return original;
        }
        return false;
    }
}
