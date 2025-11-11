package com.yiranmushroom.mixin.ite_mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.EnumQuality;
import net.minecraft.ItemStack;
import net.minecraft.SlotCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SlotCrafting.class)
@Restriction(require = @Condition(value = "mite_ite"))
public class SlotCraftingMixin {
    @WrapOperation(method = "modifyStackForRightClicks", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;setQuality(Lnet/minecraft/EnumQuality;)Lnet/minecraft/ItemStack;"))
    private ItemStack mod$modifyStackForRightClicks(ItemStack instance, EnumQuality quality, Operation<ItemStack> original) {
        return instance.setQuality(quality);
    }
}
