package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yiranmushroom.commands.IHomeCommandContext;
import com.yiranmushroom.container.IGetTrashCanInventory;
import com.yiranmushroom.container.InventoryTrashCan;
import com.yiranmushroom.enchantments.FlyingEnchantment;
import com.yiranmushroom.mixin_helper.EntityPlayerScripting;
import com.yiranmushroom.mixin_helper.ExactPlayerPositionInfo;
import net.minecraft.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.xiaoyu233.fml.FishModLoader.LOGGER;


@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase implements IHomeCommandContext, IGetTrashCanInventory {
    @Shadow
    public abstract ItemStack[] getLastActiveItems();

    @Shadow
    public abstract String getEntityName();

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

    @Inject(method = "readEntityFromNBT", at = @At("HEAD"))
    public void inj$readEntityFromNBT(net.minecraft.NBTTagCompound nbt, CallbackInfo ci) {
        if (nbt.hasKey("ith$regenerationAmount")) {
            this.mixin$regenerationAmount = nbt.getFloat("regenerationAmount");
        } else {
            this.mixin$regenerationAmount = 0f;
        }

        if (nbt.hasKey("ith$homePositionInfo")) {
            var nbtCompound = nbt.getCompoundTag("ith$homePositionInfo");
            for (Object nbtTag : nbtCompound.getTags()) {
                if (nbtTag instanceof NBTTagCompound nbtTagCompound) {
                    ith$homePositionInfo.put(nbtTagCompound.getName(), ExactPlayerPositionInfo.fromNBT(nbtTagCompound));
                }
            }
        }

        if (nbt.hasKey("ith$backPositionInfo")) {
            this.ith$backPositionInfo = ExactPlayerPositionInfo.fromNBT(nbt.getCompoundTag("ith$backPositionInfo"));
        } else {
            this.ith$backPositionInfo = null;
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At("RETURN"))
    public void inj$writeEntityToNBT(net.minecraft.NBTTagCompound nbt, CallbackInfo ci) {
        nbt.setFloat("ith$regenerationAmount", this.mixin$regenerationAmount);

        NBTTagCompound homePosNbt = new NBTTagCompound();

        for (var entry : ith$homePositionInfo.entrySet()) {
            homePosNbt.setTag(entry.getKey(), entry.getValue().toNBT());
        }

        nbt.setTag("ith$homePositionInfo", homePosNbt);

        if (this.ith$backPositionInfo != null)
            nbt.setCompoundTag("ith$backPositionInfo", this.ith$backPositionInfo.toNBT());
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void inj$constructor(World par1World, String par2Str, CallbackInfo ci) {
        LOGGER.info("EntityPlayerMixin: Constructor injected for player: {}", par2Str);

        this.mixin$trashCan.destroyInventory();
    }

    @Override
    protected void onDeathUpdate() {
        this.ith$backPositionInfo = this.getCurrentExactPositionInfo();
        super.onDeathUpdate();
    }

    @Inject(method = "clonePlayer", at = @At("RETURN"))
    public void inj$clonePlayer(EntityPlayer oldPlayer, boolean respawnFromEnd, CallbackInfo ci) {
        this.ith$homePositionInfo = ((EntityPlayerMixin) (Object) oldPlayer).ith$homePositionInfo;
        this.ith$backPositionInfo = ((EntityPlayerMixin) (Object) oldPlayer).ith$backPositionInfo;
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

    @Unique
    private Map<String, ExactPlayerPositionInfo> ith$homePositionInfo = new HashMap<>();

    @Override
    public @NotNull ExactPlayerPositionInfo getCurrentExactPositionInfo() {
        return new ExactPlayerPositionInfo(
                this.posX,
                this.posY,
                this.posZ,
                this.rotationYaw,
                this.rotationPitch,
                this.dimension
        );
    }

    @Override
    public void setPlayerToExactPositionInfo(ExactPlayerPositionInfo info) {
        if (info.getDimensionId() != this.dimension)
            this.travelToDimension(info.getDimensionId());
        this.setRotation(info.getYaw(), info.getPitch());
        this.setPositionAndUpdate(info.getX(), info.getY(), info.getZ());
    }

    @Unique
    private ExactPlayerPositionInfo ith$backPositionInfo = null;

    @Override
    public @NotNull List<String> getHomeNames() {
        return ith$homePositionInfo.keySet().stream().toList();
    }

    @Override
    public ExactPlayerPositionInfo getHomeExactPosition(@NotNull String name) {
        return ith$homePositionInfo.get(name);
    }

    @Override
    public void setHomeExactPosition(@NotNull String name, @NotNull ExactPlayerPositionInfo info) {
        ith$homePositionInfo.put(name, info);
    }

    @Override
    public boolean deleteHome(@NotNull String name) {
        return ith$homePositionInfo.remove(name) != null;
    }

    @Override
    public ExactPlayerPositionInfo getBackExactPosition() {
        return ith$backPositionInfo;
    }

    @Override
    public void setBackExactPosition(@NotNull ExactPlayerPositionInfo info) {
        ith$backPositionInfo = info;
    }

    @Unique
    private IInventory mixin$trashCan = new InventoryTrashCan();

    @Override
    public @NotNull IInventory getTrashCanInventory() {
        return this.mixin$trashCan;
    }
}
