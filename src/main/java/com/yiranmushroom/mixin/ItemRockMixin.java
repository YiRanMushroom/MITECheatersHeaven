package com.yiranmushroom.mixin;

import com.yiranmushroom.mixin_helper.ItemRockScripting;
import net.minecraft.ItemRock;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRock.class)
public class ItemRockMixin {
    @Inject(method = "getExperienceValueWhenSacrificed", at = @At("RETURN"), cancellable = true)
    private static void getExperienceValueWhenSacrificed(ItemStack item_stack, CallbackInfoReturnable<Integer> cir) {
        if (ItemRockScripting.getExperienceTransform() != null) {
            cir.setReturnValue(ItemRockScripting.getExperienceTransform().invoke(item_stack, cir.getReturnValue()));
        }
    }
}
