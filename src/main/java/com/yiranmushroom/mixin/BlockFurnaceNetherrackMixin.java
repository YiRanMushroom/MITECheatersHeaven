package com.yiranmushroom.mixin;

import com.yiranmushroom.mixin_helper.FurnaceScripting;
import net.minecraft.BlockFurnace;
import net.minecraft.BlockFurnaceNetherrack;
import net.minecraft.BlockFurnaceObsidian;
import net.minecraft.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockFurnaceNetherrack.class)
public abstract class BlockFurnaceNetherrackMixin extends BlockFurnace {
    protected BlockFurnaceNetherrackMixin(int par1, Material material, boolean par2) {
        super(par1, material, par2);
    }

    @Inject(method = "getMaxHeatLevel", at = @At("HEAD"), cancellable = true)
    public void getMaxHeatLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(FurnaceScripting.getNetherrackFurnaceMaxHeatLevel());
    }
}
