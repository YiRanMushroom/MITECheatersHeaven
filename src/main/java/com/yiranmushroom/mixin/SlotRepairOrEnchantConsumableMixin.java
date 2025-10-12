package com.yiranmushroom.mixin;

import com.yiranmushroom.MITECheatersHeaven;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlotRepairOrEnchantConsumable.class)
public abstract class SlotRepairOrEnchantConsumableMixin extends Slot {
    public SlotRepairOrEnchantConsumableMixin(IInventory inventory, int slot_index, int display_x, int display_y) {
        super(inventory, slot_index, display_x, display_y);
    }

    @Inject(method = "isItemValid", at = @At("RETURN"), cancellable = true)
    private void onIsItemValid(ItemStack item_stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && (item_stack.getItem() != Item.enchantedBook)) {
            boolean newValue = item_stack.getEnchantmentTagList() != null;
//            MITECheatersHeaven.getLOGGER().info("SlotRepairOrEnchantConsumable: isItemValid overridden for item_stack: {}, newValue: {}",
//                    item_stack, newValue);
            cir.setReturnValue(newValue);
        }
    }
}
