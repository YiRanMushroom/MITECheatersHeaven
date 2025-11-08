package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.MapGenVillage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MapGenVillage.class)
public class MapGenVillageMixin {
    @ModifyConstant(method = "canSpawnStructureAtCoords", constant = @Constant(intValue = 60, ordinal = 0), require = 0)
    private int modifyMinDistance(int constant) {
        return 0;
    }

    @ModifyExpressionValue(method = "canSpawnStructureAtCoords", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldInfo;getVillagePrerequisites()B", ordinal = 0), require = 0)
    private byte modifyVillagePrerequisite(byte original) {
        return 0;
    }
}
