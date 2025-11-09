package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.yiranmushroom.api.IIsNextChaining;
import com.yiranmushroom.enchantments.ChainingEnchantment;
import kotlin.Triple;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

@Mixin(ItemInWorldManager.class)
public abstract class ItemInWorldManagerChainingMixin implements IIsNextChaining {
    @Shadow
    public ServerPlayer thisPlayerMP;

    @Shadow
    public abstract boolean tryHarvestBlock(int x, int y, int z);

    @Shadow
    public World theWorld;
    @Unique
    private boolean ith$suppressChaining = false;

    @Inject(method = "tryHarvestBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/Block;dropBlockAsItself(Lnet/minecraft/BlockBreakInfo;)I"))
    private void ith$chaining$afterSilkTouch(int x, int y, int z,
                                             CallbackInfoReturnable<Boolean> cir,
                                             @Local(name = "block_break_info") BlockBreakInfo blockBreakInfo,
                                             @Local(name = "block") Block block) {
        if (ith$suppressChaining) {
            return;
        }

        if ((nextChainingAndNotify()
                && ChainingEnchantment.isBlockSupported(block))) {
            var positions = ith$getChainedPositions(x, y, z,
                    block);
            this.ith$suppressChaining = true;
            for (var pos : positions) {
                this.tryHarvestBlock(pos.getFirst(), pos.getSecond(), pos.getThird());
            }
            this.ith$suppressChaining = false;
        }
    }

    @Inject(method = "tryHarvestBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/Block;dropBlockAsEntityItem(Lnet/minecraft/BlockBreakInfo;)I"))
    private void ith$chaining$afterFortune(int x, int y, int z, CallbackInfoReturnable<Boolean> cir,
                                           @Local(name = "block_break_info") BlockBreakInfo blockBreakInfo,
                                           @Local(name = "block") Block block) {
        if (ith$suppressChaining) {
            return;
        }

        if ((nextChainingAndNotify()
                && ChainingEnchantment.isBlockSupported(block))) {
            var positions = ith$getChainedPositions(x, y, z,
                    block);
            this.ith$suppressChaining = true;
            for (var pos : positions) {
                this.tryHarvestBlock(pos.getFirst(), pos.getSecond(), pos.getThird());
            }
            this.ith$suppressChaining = false;
        }
    }

    @Unique
    private ArrayList<Triple<Integer, Integer, Integer>> ith$getChainedPositions(int x, int y, int z, Block block) {
        var visited = new HashSet<Triple<Integer, Integer, Integer>>();
        var toHarvest = new ArrayList<Triple<Integer, Integer, Integer>>();
        var tasks = new LinkedList<Triple<Integer, Integer, Integer>>();
        var maxChainingSize = ChainingEnchantment.maxChainingCount;
        tasks.add(new Triple<>(x, y, z));
        visited.add(new Triple<>(x, y, z));

        while (!tasks.isEmpty()) {
            var thisPos = tasks.poll();
            var thisX = thisPos.getFirst();
            var thisY = thisPos.getSecond();
            var thisZ = thisPos.getThird();

            if (!thisPos.equals(new Triple<>(x, y, z))) {
                toHarvest.add(thisPos);
            }

            visited.add(thisPos);

            for (var dx = -1; dx <= 1; dx++) {
                for (var dy = -1; dy <= 1; dy++) {
                    for (var dz = -1; dz <= 1; dz++) {
                        var neighborX = thisX + dx;
                        var neighborY = thisY + dy;
                        var neighborZ = thisZ + dz;

                        var neighborBlock = this.theWorld.getBlock(neighborX, neighborY, neighborZ);

                        var neighborPos = new Triple<>(neighborX, neighborY, neighborZ);

                        if (neighborBlock == null ||
                                !ChainingEnchantment.areTwoBlockSimilar(block, neighborBlock) ||
                                (dx == 0 && dy == 0 && dz == 0) ||
                                !visited.add(neighborPos)) {
                            continue;
                        }

                        if (toHarvest.size() < maxChainingSize) {
                            tasks.add(neighborPos);
                            toHarvest.add(neighborPos);
                        } else {
                            return toHarvest;
                        }
                    }
                }
            }
        }
        return toHarvest;
    }

    boolean ith$nextChaining = false;

    @Override
    public boolean isNextChaining() {
        return ith$nextChaining;
    }

    @Override
    public void setNextChaining(boolean nextChaining) {
        ith$nextChaining = nextChaining;
    }

    @Override
    public void notifyChainingDone() {
        ith$nextChaining = false;
    }

    private boolean nextChainingAndNotify() {
        boolean result = ith$nextChaining;
        ith$nextChaining = false;
        return result;
    }
}
