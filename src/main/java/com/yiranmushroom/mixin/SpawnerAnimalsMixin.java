package com.yiranmushroom.mixin;

import com.yiranmushroom.mixin_helper.SpawnerAnimalsScripting;
import net.minecraft.EnumCreatureType;
import net.minecraft.SpawnerAnimals;
import net.minecraft.World;
import net.minecraft.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnerAnimals.class)
public class SpawnerAnimalsMixin {
    @Redirect(
            method = "performRandomLivingEntitySpawning",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/SpawnerAnimals;isBlueMoonAnimalSpawningPeriod(Lnet/minecraft/World;)Z")
    )
    private boolean isBlueMoonAnimalSpawningPeriod(SpawnerAnimals instance, World world) {
        return true;
    }

    @Redirect(
            method = "performRandomLivingEntitySpawning",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldServer;getTimeOfDay()I", ordinal = 2)
    )
    private int getTimeOfDay(WorldServer instance) {
        var originalTime = instance.getTimeOfDay();
        if (originalTime % SpawnerAnimalsScripting.getAnimalSpawnTickInterval() == 0) {
            return 0;
        } else {
            return originalTime;
        }
    }

    @ModifyConstant(method = "trySpawningPeacefulMobs", constant = @Constant(floatValue = 576.0F))
    private float mod$min_distance_from_spawn_sq(float original) {
        return 0;
    }

    @ModifyConstant(method = "trySpawningPeacefulMobs", constant = @Constant(floatValue = 24.0F))
    private float mod$min_distance_from_players(float original) {
        return 0;
    }

    @Inject(method = "trySpawningPeacefulMobs", at = @At("HEAD"))
    private void inj$trySpawningPeacefulMobs(WorldServer world, EnumCreatureType creature_type, CallbackInfoReturnable<Integer> cir) {
        SpawnerAnimalsScripting.getOnTrySpawnPeacefulMob().invoke(world, creature_type);
    }

    @Redirect(method = "trySpawningPeacefulMobs", at = @At(value = "INVOKE", target = "Lnet/minecraft/SpawnerAnimals;isBlueMoonAnimalSpawningPeriod(Lnet/minecraft/World;)Z"))
    private boolean isBlueMoonAnimalSpawningPeriod2(SpawnerAnimals instance, World world) {
        return true;
    }

    @Redirect(method = "trySpawningPeacefulMobs", at = @At(value = "INVOKE", target = "Lnet/minecraft/EnumCreatureType;getMaxNumberOfCreature()I"))
    private int getMaxNumberOfCreature(EnumCreatureType instance) {
        return (int) SpawnerAnimalsScripting.getAnimalSpawnMultiplier() * instance.getMaxNumberOfCreature();
    }
}
