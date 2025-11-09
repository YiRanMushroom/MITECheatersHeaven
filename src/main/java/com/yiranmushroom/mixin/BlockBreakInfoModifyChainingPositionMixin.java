package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yiranmushroom.mixin_helper.ChainingDropPositionLockHelper;
import net.minecraft.BlockBreakInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBreakInfo.class)
public class BlockBreakInfoModifyChainingPositionMixin {
    @ModifyExpressionValue(method = "createEntityItem", at = @At(value = "FIELD", target = "Lnet/minecraft/BlockBreakInfo;x:I"))
    private int ith$modifyChainingX(int originalX) {
        return ChainingDropPositionLockHelper.isPositionLocked ? ChainingDropPositionLockHelper.lockedX : originalX;
    }

    @ModifyExpressionValue(method = "createEntityItem", at = @At(value = "FIELD", target = "Lnet/minecraft/BlockBreakInfo;y:I"))
    private int ith$modifyChainingY(int originalY) {
        return ChainingDropPositionLockHelper.isPositionLocked ? ChainingDropPositionLockHelper.lockedY : originalY;
    }

    @ModifyExpressionValue(method = "createEntityItem", at = @At(value = "FIELD", target = "Lnet/minecraft/BlockBreakInfo;z:I"))
    private int ith$modifyChainingZ(int originalZ) {
        return ChainingDropPositionLockHelper.isPositionLocked ? ChainingDropPositionLockHelper.lockedZ : originalZ;
    }
}
