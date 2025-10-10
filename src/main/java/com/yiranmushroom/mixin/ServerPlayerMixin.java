package com.yiranmushroom.mixin;

import net.minecraft.GameRules;
import net.minecraft.ServerPlayer;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
}
