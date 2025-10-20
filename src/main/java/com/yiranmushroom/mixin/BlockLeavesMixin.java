package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.yiranmushroom.mixin_helper.LeavesScripting;
import net.minecraft.BlockBreakInfo;
import net.minecraft.BlockLeaves;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockLeaves.class)
public class BlockLeavesMixin {
    @WrapOperation(method = "dropBlockAsEntityItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/BlockLeaves;dropBlockAsEntityItem(Lnet/minecraft/BlockBreakInfo;IIIF)I"))
    private int modifyDropChance(BlockLeaves instance, BlockBreakInfo blockBreakInfo, int id, int subtype, int quantity, float chance, Operation<Integer> original) {
        if (LeavesScripting.getRedirectedSaplingDropFunction() == null) {
            return original.call(instance, blockBreakInfo, id, subtype, quantity, chance);
        } else {
            return LeavesScripting.getRedirectedSaplingDropFunction().
                    invoke(instance, blockBreakInfo, id, subtype, quantity, chance, original::call);
        }
    }
}
