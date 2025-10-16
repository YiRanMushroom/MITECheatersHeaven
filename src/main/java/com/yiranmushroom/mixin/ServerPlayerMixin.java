package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.Enchantment;
import net.minecraft.EnchantmentHelper;
import net.minecraft.GameRules;
import net.minecraft.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.xiaoyu233.fml.FishModLoader.LOGGER;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/GameRules;getGameRuleBooleanValue(Ljava/lang/String;)Z"))
    public boolean injectKeepInventory(GameRules instance, String key) {
        if (key.equals("keepInventory")) {
            return true;
        } else {
            return instance.getGameRuleBooleanValue(key);
        }
    }

    @Unique
    private long mixin$recordFoodExhaustionTime = 0;

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    public void inj$readEntityFromNBT(net.minecraft.NBTTagCompound nbt, CallbackInfo ci) {
        if (nbt.hasKey("recordFoodExhaustionTime")) {
            this.mixin$recordFoodExhaustionTime = nbt.getLong("recordFoodExhaustionTime");
        } else {
            this.mixin$recordFoodExhaustionTime = 0;
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    public void inj$writeEntityToNBT(net.minecraft.NBTTagCompound nbt, CallbackInfo ci) {
        nbt.setLong("recordFoodExhaustionTime", this.mixin$recordFoodExhaustionTime);
    }

    @Unique
    private boolean mixin$ShouldDecrease() {
        if (this.mixin$recordFoodExhaustionTime == 0) {
            long enduranceLevel = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.endurance.effectId, ((ServerPlayer) (Object) this).getLastActiveItems());
            if (enduranceLevel >= 63) {
                mixin$recordFoodExhaustionTime = Long.MAX_VALUE;
            } else {
                mixin$recordFoodExhaustionTime = (long) Math.pow(2, enduranceLevel);
            }

            return true;
        } else {
            this.mixin$recordFoodExhaustionTime--;
            return false;
        }
    }

    @ModifyExpressionValue(method = "decrementNutrients", at = @At(value = "INVOKE", target = "Lnet/minecraft/ServerPlayer;inCreativeMode()Z", ordinal = 0))
    public boolean modifyDecrementNutrientsInCreativeMode(boolean original) {
//        LOGGER.info("modifyDecrementNutrientsInCreativeMode called, original: " + original);
        return original || !this.mixin$ShouldDecrease();
    }
}
