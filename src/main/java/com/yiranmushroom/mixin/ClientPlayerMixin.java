package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yiranmushroom.container.GuiTrashCan;
import com.yiranmushroom.container.IGetTrashCanInventory;
import com.yiranmushroom.container.IOpenTrashCan;
import com.yiranmushroom.enchantments.FlyingEnchantment;
import com.yiranmushroom.mixin_helper.ClientPlayerScripting;
import net.minecraft.ClientPlayer;
import net.minecraft.EntityPlayer;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayer.class)
public abstract class ClientPlayerMixin implements IOpenTrashCan, IGetTrashCanInventory {
    @Shadow
    protected Minecraft mc;

    @Inject(method = "getCraftingPeriod", at = @At(value = "RETURN"), cancellable = true)
    private void modify$getCraftingPeriod(float quality_adjusted_crafting_difficulty, CallbackInfoReturnable<Integer> cir) {
        var transform = ClientPlayerScripting.getCraftingPeriodTransform();
        if (transform != null) {
            var result = transform.invoke((ClientPlayer) (Object) this, ((ClientPlayer) (Object) this).openContainer, cir.getReturnValueI());
            cir.setReturnValue(result);
        } // else do nothing
    }

    @ModifyExpressionValue(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/PlayerCapabilities;allowFlying:Z"))
    private boolean modify$allowFlying(boolean original) {
        return original || FlyingEnchantment.holdBy((ClientPlayer) (Object) this);
    }

    @Override
    public void openTrashCan() {
        this.mc.displayGuiScreen(new GuiTrashCan((EntityPlayer) (Object) this, this.getTrashCanInventory()));
    }
}
