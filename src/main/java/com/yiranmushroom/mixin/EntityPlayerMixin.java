package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yiranmushroom.enchantments.FlyingEnchantment;
import com.yiranmushroom.mixin_helper.EntityPlayerScripting;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase {
    @Shadow
    public abstract ItemStack[] getLastActiveItems();

    public EntityPlayerMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "getReach(Lnet/minecraft/EnumEntityReachContext;Lnet/minecraft/Entity;)F", at = @At("RETURN"), cancellable = true)
    void inj$getReach1(EnumEntityReachContext context, Entity entity, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(EntityPlayerScripting.getGetReachModify1().invoke((EntityPlayer) (Object) this, context, entity, cir.getReturnValueF()));
    }

    @Inject(method = "getReach(Lnet/minecraft/Block;I)F", at = @At("RETURN"), cancellable = true)
    void inj$getReach2(net.minecraft.Block block, int meta, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(EntityPlayerScripting.getGetReachModify2().invoke((EntityPlayer) (Object) this, block, meta, cir.getReturnValueF()));
    }

    @Inject(method = "getRelativeBlockHardness", at = @At("RETURN"), cancellable = true)
    void inj$getRelativeBlockHardness(int x, int y, int z, boolean apply_held_item, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(EntityPlayerScripting.getGetRelativeBlockHardnessModify().invoke((EntityPlayer) (Object) this, x, y, z, apply_held_item, cir.getReturnValueF()));
    }

    @Inject(method = "getMaxCraftingQuality", at = @At("RETURN"), cancellable = true)
    void inj$getMaxCraftingQuality(float unadjusted_crafting_difficulty_to_produce, Item item, int[] applicable_skillsets, CallbackInfoReturnable<EnumQuality> cir) {
        cir.setReturnValue(EntityPlayerScripting.getGetMaxCraftingQualityModify().invoke((EntityPlayer) (Object) this, unadjusted_crafting_difficulty_to_produce, item, applicable_skillsets, cir.getReturnValue()));
    }

    @Inject(method = "getMinCraftingQuality", at = @At("RETURN"), cancellable = true)
    void inj$getMinCraftingQuality(Item item, int[] applicable_skillsets, CallbackInfoReturnable<EnumQuality> cir) {
        cir.setReturnValue(EntityPlayerScripting.getGetMinCraftingQualityModify().invoke((EntityPlayer) (Object) this, item, applicable_skillsets, cir.getReturnValue()));
    }

    @Redirect(method = {"onDeath", "getExperienceValue", "clonePlayer"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/GameRules;getGameRuleBooleanValue(Ljava/lang/String;)Z"))
    public boolean injectKeepInventory(GameRules gameRules, String key) {
        if (key.equals("keepInventory")) {
            return true;
        } else {
            return gameRules.getGameRuleBooleanValue(key);
        }
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    public void inj$readEntityFromNBT(net.minecraft.NBTTagCompound nbt, CallbackInfo ci) {
        if (nbt.hasKey("regenerationAmount")) {
            this.mixin$regenerationAmount = nbt.getFloat("regenerationAmount");
        } else {
            this.mixin$regenerationAmount = 0f;
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    public void inj$writeEntityToNBT(net.minecraft.NBTTagCompound nbt, CallbackInfo ci) {
        nbt.setFloat("regenerationAmount", this.mixin$regenerationAmount);
    }

    @Unique
    private static final float mixin$maxGenerationTime = 20 * 60;

    @Unique
    private float mixin$regenerationAmount = 0.0f;

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    void inj$onLivingUpdate(CallbackInfo ci) {
        int levelOfRegeneration = EnchantmentHelper.getMaxEnchantmentLevel(Enchantment.regeneration.effectId, ((EntityPlayer) (Object) this).getLastActiveItems());
        mixin$regenerationAmount += (float) levelOfRegeneration / mixin$maxGenerationTime;
        if (mixin$regenerationAmount >= 1.0f) {
            this.heal((int) mixin$regenerationAmount);
            mixin$regenerationAmount = 0.0f;
        }
    }

    @ModifyExpressionValue(method = "fall", at = @At(value = "FIELD", target = "Lnet/minecraft/PlayerCapabilities;allowFlying:Z"))
    private boolean modify$allowFlying(boolean original) {
        return original || FlyingEnchantment.holdBy((EntityPlayer) (Object) this);
    }
}
