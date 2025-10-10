package com.yiranmushroom.mixin;

import com.yiranmushroom.item.Items;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityHopper.class)
public abstract class TileEntityHopperMixin extends TileEntity implements Hopper {
    @Shadow
    public abstract double getXPos();

    @Shadow
    public abstract ItemStack getStackInSlot(int par1);

    @Inject(method = "insertItemToInventory", at = @At("RETURN"), cancellable = true)
    private void insertItemToInventoryInject(CallbackInfoReturnable<Boolean> callback) {
        // check if the block above it is lava or flowing lava
        var blockAbove = this.getWorldObj().getBlock((int) this.getXPos(), (int) this.getYPos() + 1, (int) this.getZPos());
        if (blockAbove != null && (blockAbove.blockID == Block.lavaStill.blockID || blockAbove.blockID == Block.lavaMoving.blockID)) {
            if (this.getStackInSlot(0) == null) {
                this.setInventorySlotContents(0, new ItemStack(Items.getLavaInPipes(), 1));
            }
        }
    }

    @Shadow
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
    }
}
