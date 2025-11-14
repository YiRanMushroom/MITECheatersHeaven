package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.BlockBreakInfo;
import net.minecraft.BlockTallGrass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockTallGrass.class)
public class BlockTallGrassMixin {
    @WrapOperation(method = "dropBlockAsEntityItem", at =
    @At(value = "INVOKE", target = "Lnet/minecraft/BlockTallGrass;dropBlockAsEntityItem(Lnet/minecraft/BlockBreakInfo;IIIF)I"))
    private int ith$wrapDropBlockAsEntityItem(BlockTallGrass instance, BlockBreakInfo blockBreakInfo, int id, int subtype, int quantity, float chance, Operation<Integer> original) {
        return original.call(instance, blockBreakInfo, id, subtype, quantity, Math.min(chance * 4.0f, 1.0f));
    }
}
