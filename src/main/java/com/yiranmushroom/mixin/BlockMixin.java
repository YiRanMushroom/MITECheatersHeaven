package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.Block;
import net.minecraft.BlockBreakInfo;
import net.minecraft.EntityItem;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow
    protected abstract EntityItem dropBlockAsItem_do(BlockBreakInfo info, ItemStack item_stack);

    @Inject(method = "dropBlockAsEntityItem(Lnet/minecraft/BlockBreakInfo;IIIF)I", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F", ordinal = 1))
    private void ith$inj$modifyFortuneDropCount(BlockBreakInfo info, int id_dropped, int subtype, int quantity, float par_chance, CallbackInfoReturnable<Integer> cir,
                                                @Local(name = "chance") LocalFloatRef chance,
                                                @Local(name = "item_stack") ItemStack item_stack,
                                                @Local(name = "damage") int damage) {
//        chance.set(chance.get() - 1.0f); // Decrease the chance by 1.0f, because it is already dropped one.
        var mean = chance.get();
        chance.set(0.0f);

        var standardDeviation = mean / 2.0f; // Arbitrary choice for standard deviation

        // use gaussian distribution to determine the number of extra drops
        var extraDrops = (int) Math.round((info.world.rand.nextGaussian() * standardDeviation) + mean);

        for (int i = 0; i < extraDrops; i++) {
            var entityItem = dropBlockAsItem_do(info, item_stack.copy());
            if (damage != 0) {
                entityItem.getEntityItem().setItemDamage(damage);
            }
        }
    }

}
