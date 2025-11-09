/*
package com.yiranmushroom.mixin;

import com.yiranmushroom.config.MITECheatersHeavenConfig;
import com.yiranmushroom.enchantments.Enchantments;
import com.yiranmushroom.enchantments.IDoChaining;
import net.minecraft.EnchantmentHelper;
import net.minecraft.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayer.class)
public class ServerPlayerCheckChainingMixin implements IDoChaining {
    @Unique
    private boolean ith$wantToChain = false;

    @Override
    @SuppressWarnings("All")
    public boolean doChaining() {
        return ith$wantToChain && (
                MITECheatersHeavenConfig.getAlwaysAllowChaining().getBooleanValue() ||
                        EnchantmentHelper.getEnchantmentLevel(Enchantments.chainingEnchant,
                                ((ServerPlayer) (Object) this).getHeldItemStack()) > 0
        );
    }

    @Override
    public void requestChaining(boolean on) {
        ith$wantToChain = on;
    }
}
*/
