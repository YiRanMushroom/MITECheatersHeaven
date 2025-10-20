package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.yiranmushroom.mixin_helper.GrowingPlantScripting;
import net.minecraft.BlockGrowingPlant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockGrowingPlant.class)
public class GrowingPlantMixin {
    @ModifyReturnValue(method = "getGlobalGrowthRateModifierFromMITE", at = @At("RETURN"))
    private float modifyGrowthRate(float originalRate) {
        if (GrowingPlantScripting.getGlobalGrowingSpeedTransformer() != null) {
            return GrowingPlantScripting.getGlobalGrowingSpeedTransformer().invoke((BlockGrowingPlant) (Object) this, originalRate);
        }
        return originalRate;
    }

    @ModifyReturnValue(method = "isLightLevelSuitableForGrowth", at = @At("RETURN"))
    private boolean modifyLightLevelSuitability(boolean originalSuitability) {
        if (GrowingPlantScripting.getCheckLightLevelTransformer() != null) {
            return GrowingPlantScripting.getCheckLightLevelTransformer().invoke((BlockGrowingPlant) (Object) this, originalSuitability);
        }
        return originalSuitability;
    }
}
