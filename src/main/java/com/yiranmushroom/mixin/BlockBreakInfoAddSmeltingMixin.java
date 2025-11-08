package com.yiranmushroom.mixin;

import com.yiranmushroom.api.ISmeltingInfo;
import com.yiranmushroom.enchantments.Enchantments;
import com.yiranmushroom.enchantments.OreSmeltingEnchantment;
import net.minecraft.BlockBreakInfo;
import net.minecraft.Enchantment;
import net.minecraft.EnchantmentHelper;
import net.minecraft.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBreakInfo.class)
public class BlockBreakInfoAddSmeltingMixin implements ISmeltingInfo {
    @Unique
    private int ite$smeltingHarvestedLevel = 0;

    @Override
    public boolean isSmeltingHarvested() {
        return ite$smeltingHarvestedLevel > 0;
    }

    @Override
    public int smeltingHarvestedLevel() {
        return ite$smeltingHarvestedLevel;
    }

    @Shadow
    private boolean was_silk_harvested;

    @Inject(method = "setHarvestedBy", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivingBase;canSilkHarvestBlock(Lnet/minecraft/Block;I)Z", shift = At.Shift.AFTER))
    private void ith$inj$setSmeltingHarvested(EntityLivingBase harvesting_entity, CallbackInfoReturnable<BlockBreakInfo> cir) {
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch, harvesting_entity.getHeldItemStack()) != 0) {
            return;
        }

        // Testing
        var level = OreSmeltingEnchantment.getHoldingOreSmeltingEnchantmentLevel(harvesting_entity);

        switch (level) {
            case 0 -> ite$smeltingHarvestedLevel = 0;
            case 1 -> ite$smeltingHarvestedLevel = 1;
            case 2 -> ite$smeltingHarvestedLevel = 3;
            case 3 -> ite$smeltingHarvestedLevel = Integer.MAX_VALUE;
            default -> {
                if (level > 3) {
                    ite$smeltingHarvestedLevel = Integer.MAX_VALUE;
                } else {
                    ite$smeltingHarvestedLevel = 0;
                }
            }
        }
    }
}
