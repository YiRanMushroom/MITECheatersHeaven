package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.yiranmushroom.api.ISmeltingInfo;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockAddSmeltingMixin {
    @Inject(method = "dropBlockAsItem_do", at = @At("HEAD"))
    private void ite$handleSmelting$dropBlockAsItem_do(BlockBreakInfo info, ItemStack item_stack,
                                                       CallbackInfoReturnable<EntityItem> cir
//                                                       , @Local(ordinal = 0, argsOnly = true) LocalRef<ItemStack> itemStackRef
    ) {
        ISmeltingInfo smeltingInfo = (ISmeltingInfo) info;

        if (!smeltingInfo.isSmeltingHarvested()) {
            return;
        }

        int multiplier = 1;
        ItemStack copied = item_stack.copy();
        multiplier = copied.stackSize;
        copied.stackSize = 1;

        var smeltedResult = FurnaceRecipes.smelting().getSmeltingResult(copied, smeltingInfo.smeltingHarvestedLevel());

        if (smeltedResult == null) {
            return;
        }

        smeltedResult.stackSize *= multiplier;

        item_stack.stackSize = smeltedResult.stackSize;
        item_stack.itemID = smeltedResult.itemID;

        if (item_stack.getExperienceReward() > 0) {
            info.world.spawnEntityInWorld(
                    new EntityXPOrb(info.world, info.x, info.y, info.z, item_stack.getExperienceReward()));
        }
    }
}
