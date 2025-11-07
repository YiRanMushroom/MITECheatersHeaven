package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixinRemoveFire {
    @ModifyExpressionValue(method = "dropItemStack(Lnet/minecraft/ItemStack;F)Lnet/minecraft/EntityItem;", at = @At(value = "INVOKE", target = "Lnet/minecraft/Entity;isBurning()Z"))
    private boolean ith$cancelFireAspectBurnItem$isBurning(boolean original) {
        return false;
    }
}
