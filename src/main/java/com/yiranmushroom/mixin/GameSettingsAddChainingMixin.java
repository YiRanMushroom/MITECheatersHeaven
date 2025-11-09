package com.yiranmushroom.mixin;

import com.yiranmushroom.config.MITECheatersHeavenConfig;
import net.minecraft.GameSettings;
import net.minecraft.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Mixin(GameSettings.class)
public class GameSettingsAddChainingMixin {
    @Shadow
    public KeyBinding[] keyBindings;

    @Inject(method = "initKeybindings", at = @At("RETURN"))
    private void addChainingKeyBinding(CallbackInfo ci) {
        List<KeyBinding> list = new LinkedList<>(Arrays.asList(keyBindings));
        list.add(MITECheatersHeavenConfig.getEnableChainingHotkey());
        keyBindings = list.toArray(KeyBinding[]::new);
    }
}
