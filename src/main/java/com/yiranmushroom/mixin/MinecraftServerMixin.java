package com.yiranmushroom.mixin;

import com.yiranmushroom.scripting.ScriptingEngine;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Inject(at = @At("HEAD"), method = "loadAllWorlds")
	private void init(CallbackInfo info) {
		// Must wait for scripting engine finish initialization
        ScriptingEngine.waitForInitialization();
	}
}