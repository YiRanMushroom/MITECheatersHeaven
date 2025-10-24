package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.ItemBow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemBow.class)
public class ItemBowMixin {
    @ModifyReturnValue(method = "getTicksForMaxPull", at = @At("RETURN"))
    private static int modifyGetMaxItemUseDuration(int originalDuration) {
//        return Math.min(Math.max(originalDuration, 1), 20);
        return originalDuration;
    }
}
