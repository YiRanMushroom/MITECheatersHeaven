package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.yiranmushroom.MITECheatersHeaven;
import com.yiranmushroom.enchantments.Enchantments;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ContainerRepair.class)
public class ContainerRepairMixin {
    @Inject(method = "updateRepairOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;isItemEnchanted()Z",
            ordinal = 1))
    private void onUpdateRepairOutput(
            CallbackInfo ci,
            @Local(ordinal = 0) ItemStack firstSlotItemStack,
            @Local(ordinal = 1) ItemStack secondSlotItemStack,
            @Local(ordinal = 3) LocalBooleanRef isEnchantingRef,
            @Local(ordinal = 0) LocalRef<Map> enchantmentOnItem2Ref) {
//        var logger = MITECheatersHeaven.getLOGGER();
//        logger.info("First Slot ItemStack: {}", firstSlotItemStack);
//        logger.info("Second Slot ItemStack: {}", secondSlotItemStack);
//        logger.info("Is Enchanting Ref: {}", isEnchantingRef.get());
//        logger.info("Enchantment On Item 2 Ref: {}", enchantmentOnItem2Ref.get());

        if (firstSlotItemStack.getItem().getItemEnchantability() <= 0 || secondSlotItemStack == null) {
            return;
        }

        // first is enchantable, second is not null
        NBTTagList storedEnchantments = secondSlotItemStack.getItem() == Item.enchantedBook ?
                secondSlotItemStack.getStoredEnchantmentTagList() : secondSlotItemStack.getEnchantmentTagList();

        if (storedEnchantments == null ||
                !EnchantmentHelper.hasValidEnchantmentForItem(storedEnchantments, firstSlotItemStack.getItem())) {
            return;
        }

        isEnchantingRef.set(true);
        enchantmentOnItem2Ref.set(EnchantmentHelper.getEnchantmentsMapFromTags(storedEnchantments));
//        logger.info("enchantmentOnItem2Ref updated: {}", enchantmentOnItem2Ref.get());
    }

    @Inject(method = "updateRepairOutput", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/EnchantmentHelper;getEnchantmentsMap(Lnet/minecraft/ItemStack;)Ljava/util/Map;"))
    private void onUpdateRepairOutputAfterGetEnchantmentsMap(
            CallbackInfo ci,
            @Local(ordinal = 1) Map enchantmentsOnCopyOfFirstSlot,
            @Local(ordinal = 0) LocalRef<Map> enchantmentOnItem2,
            @Local(ordinal = 2) ItemStack copyOfFirstSlot) {
//        var logger = MITECheatersHeaven.getLOGGER();
//        logger.info("Enchantments On Copy Of First Slot Ref before: {}", enchantmentsOnCopyOfFirstSlot);
//        logger.info("Enchantment On Item 2 Ref: {}", enchantmentOnItem2);
//        logger.info("Copy Of First Slot ItemStack: {}", copyOfFirstSlot);

        for (Object entryObject : enchantmentOnItem2.get().entrySet()) {
            Map.Entry entry = (Map.Entry) entryObject;
            Integer enchantmentId = (Integer) entry.getKey();
            Integer sacrificialLevel = (Integer) entry.getValue();
            Enchantment enchantment = Enchantment.enchantmentsList[enchantmentId];

            if (enchantmentsOnCopyOfFirstSlot.containsKey(enchantmentId)) {
                Integer currentLevel = (Integer) enchantmentsOnCopyOfFirstSlot.get(enchantmentId);
                enchantmentsOnCopyOfFirstSlot.put(enchantmentId, Enchantments.getAnvilEnchantmentLevelTransformHandler()
                        .transform(enchantmentId, currentLevel + sacrificialLevel));
            } else if (enchantment.canEnchantItem(copyOfFirstSlot.getItem())) {
                enchantmentsOnCopyOfFirstSlot.put(enchantmentId, Enchantments.getAnvilEnchantmentLevelTransformHandler()
                        .transform(enchantmentId, sacrificialLevel));
            }
        }

        enchantmentOnItem2.set(new HashMap<>());
    }
}
