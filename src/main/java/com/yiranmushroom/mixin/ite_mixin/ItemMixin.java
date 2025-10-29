package com.yiranmushroom.mixin.ite_mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Item.class, priority = 9999)
public class ItemMixin {
    @WrapOperation(method = "getMaxDamage(Lnet/minecraft/EnumQuality;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/Item;hasQuality()Z"))
    private boolean modifyHasQuality(Item instance, Operation<Boolean> original) {
        return instance.hasQuality();
    }
}
