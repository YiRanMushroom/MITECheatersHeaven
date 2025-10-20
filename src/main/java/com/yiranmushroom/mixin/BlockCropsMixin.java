package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.yiranmushroom.mixin_helper.GrowingPlantScripting;
import net.minecraft.BlockCrops;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockCrops.class)
public class BlockCropsMixin {
    @ModifyReturnValue(method = "chanceOfBlightPerRandomTick", at = @At("RETURN"))
    private float modifyChanceOfBlight(float originalChance) {
        if (GrowingPlantScripting.getChanceOfBlightTransformer() == null) {
            return originalChance;
        }
        return GrowingPlantScripting.getChanceOfBlightTransformer().invoke((BlockCrops) (Object) this, originalChance);
    }
}
