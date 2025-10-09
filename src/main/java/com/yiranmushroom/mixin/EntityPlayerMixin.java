package com.yiranmushroom.mixin;

import com.yiranmushroom.mixin_helper.EntityPlayerScripting;
import net.minecraft.Entity;
import net.minecraft.EntityPlayer;
import net.minecraft.EnumEntityReachContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin {
    @Inject(method = "getReach(Lnet/minecraft/EnumEntityReachContext;Lnet/minecraft/Entity;)F", at = @At("RETURN"), cancellable = true)
    void inj$getReach1(EnumEntityReachContext context, Entity entity, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(EntityPlayerScripting.getGetReach1().invoke((EntityPlayer) (Object) this, context, entity, cir.getReturnValueF()));
    }

    @Inject(method = "getReach(Lnet/minecraft/Block;I)F", at = @At("RETURN"), cancellable = true)
    void inj$getReach2(net.minecraft.Block block, int meta, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(EntityPlayerScripting.getGetReach2().invoke((EntityPlayer) (Object) this, block, meta, cir.getReturnValueF()));
    }

    @Inject(method = "getRelativeBlockHardness", at = @At("RETURN"), cancellable = true)
    void inj$getRelativeBlockHardness(int x, int y, int z, boolean apply_held_item, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(EntityPlayerScripting.getGetRelativeBlockHardness().invoke((EntityPlayer) (Object) this, x, y, z, apply_held_item, cir.getReturnValueF()));
    }
}
