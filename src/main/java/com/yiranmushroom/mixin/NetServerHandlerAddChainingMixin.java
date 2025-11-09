package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.yiranmushroom.MITECheatersHeaven;
import com.yiranmushroom.api.IIsChaining;
import com.yiranmushroom.api.IIsNextChaining;
import net.minecraft.EnumFace;
import net.minecraft.ItemInWorldManager;
import net.minecraft.NetServerHandler;
import net.minecraft.Packet85SimpleSignal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetServerHandler.class)
public class NetServerHandlerAddChainingMixin {
    @WrapOperation(method = "handleDiggingPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemInWorldManager;tryHarvestBlock(III)Z"))
    private boolean ith$wrapTryHarvestAddChaining(ItemInWorldManager instance, int dy, int felling, int held_item_stack,
                                                  Operation<Boolean> original,
                                                  @Local(argsOnly = true) Packet85SimpleSignal packet) {
        if (instance instanceof IIsNextChaining nextChaining) {
            nextChaining.setNextChaining(((IIsChaining) packet).isChaining());
        } else {
            MITECheatersHeaven.getLOGGER().warn("ItemInWorldManager is not an instance of IIsNextChaining!");
        }

        var result = original.call(instance, dy, felling, held_item_stack);

        if (instance instanceof IIsNextChaining nextChainingReset) {
            nextChainingReset.notifyChainingDone();
        }

        return result;
    }

    @WrapOperation(method = "handleDiggingPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemInWorldManager;onBlockClicked(IIILnet/minecraft/EnumFace;)V"))
    private void ith$wrapOnBlockClickedAddChaining(ItemInWorldManager instance, int x, int y, int z, EnumFace face,
                                                   Operation<Void> original,
                                                   @Local(argsOnly = true) Packet85SimpleSignal packet) {
        if (instance instanceof IIsNextChaining nextChaining) {
            nextChaining.setNextChaining(((IIsChaining) packet).isChaining());
        } else {
            MITECheatersHeaven.getLOGGER().warn("ItemInWorldManager is not an instance of IIsNextChaining!");
        }

        original.call(instance, x, y, z, face);

        if (instance instanceof IIsNextChaining nextChainingReset) {
            nextChainingReset.notifyChainingDone();
        }
    }
}
