package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yiranmushroom.container.IClearTrashCan;
import com.yiranmushroom.container.IGetTrashCanInventory;
import com.yiranmushroom.container.IOpenTrashCan;
import com.yiranmushroom.network.S2C.S2COpenTrashCanPacket;
import com.yiranmushroom.network.S2C.S2CTrashCanClearedPacket;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends EntityPlayer implements IClearTrashCan, IGetTrashCanInventory, IOpenTrashCan, ICrafting {
    public ServerPlayerMixin(World par1World, String par2Str) {
        super(par1World, par2Str);
    }

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
        return original || !this.mixin$ShouldDecrease();
    }

    @Override
    public void clearTrashCan() {
        this.getTrashCanInventory().destroyInventory();

        Network.sendToClient((ServerPlayer) (Object) this, new S2CTrashCanClearedPacket());
    }

    @Shadow
    protected abstract void incrementWindowID();

    @Shadow
    private int currentWindowId;

    @Shadow
    private NetServerHandler playerNetServerHandler;

    @Override
    public void openTrashCan() {
        if (this.openContainer != this.inventoryContainer) {
            this.closeScreen();
        }

        this.incrementWindowID();
        this.openContainer = new ContainerChest(this, this.getTrashCanInventory());
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.addCraftingToCrafters(this);

        Network.sendToClient((ServerPlayer) (Object) this, new S2COpenTrashCanPacket());
        this.playerNetServerHandler.sendPacketToPlayer((new Packet100OpenWindow(this.currentWindowId, 0,
                this.getTrashCanInventory().getCustomNameOrUnlocalized(), this.getTrashCanInventory().getSizeInventory(),
                this.getTrashCanInventory().hasCustomName())).setCoords((int) this.posX, (int) this.posY, (int) this.posZ));
    }
}
