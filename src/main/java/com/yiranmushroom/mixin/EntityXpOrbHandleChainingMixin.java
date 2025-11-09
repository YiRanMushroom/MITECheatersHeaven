package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.yiranmushroom.mixin_helper.ChainingDropPositionLockHelper;
import net.minecraft.EntityXPOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityXPOrb.class)
public class EntityXpOrbHandleChainingMixin {
    @WrapOperation(method = "<init>(Lnet/minecraft/World;DDDI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityXPOrb;setPosition(DDD)V"))
    private void ith$modifyChainingDropPosition(EntityXPOrb instance, double x, double y, double z, Operation<Void> original) {
        if (ChainingDropPositionLockHelper.isPositionLocked) {
            original.call(instance,
                    ChainingDropPositionLockHelper.lockedX + 0.5,
                    ChainingDropPositionLockHelper.lockedY + 0.5,
                    ChainingDropPositionLockHelper.lockedZ + 0.5);
        } else {
            original.call(instance, x, y, z);
        }
    }
}
