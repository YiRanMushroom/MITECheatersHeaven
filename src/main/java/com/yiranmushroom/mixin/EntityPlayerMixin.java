package com.yiranmushroom.mixin;

import com.google.gson.Gson;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yiranmushroom.commands.HomeCommandContexts;
import com.yiranmushroom.commands.IHomeCommandContext;
import com.yiranmushroom.enchantments.FlyingEnchantment;
import com.yiranmushroom.mixin_helper.EntityPlayerScripting;
import kotlin.Pair;
import kotlin.Triple;
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

import javax.security.auth.callback.Callback;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.xiaoyu233.fml.FishModLoader.LOGGER;


@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase implements IHomeCommandContext {
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
        if (nbt.hasKey("regenerationAmount")) {
            this.mixin$regenerationAmount = nbt.getFloat("regenerationAmount");
        } else {
            this.mixin$regenerationAmount = 0f;
        }

        if (nbt.hasKey("homeCommandContext")) {
            String json = nbt.getString("homeCommandContext");
            Gson gson = new Gson();
            Map<String, List<Double>> tempMap = gson.fromJson(json, Map.class);
            for (Map.Entry<String, List<Double>> entry : tempMap.entrySet()) {
                List<Double> coords = entry.getValue();
                if (coords.size() == 3) {
                    mixin$homeCommandContext.put(entry.getKey(), new Triple<>(coords.get(0), coords.get(1), coords.get(2)));
                }
            }
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void inj$constructor(World par1World, String par2Str, CallbackInfo ci) {
        LOGGER.info("EntityPlayerMixin: Constructor injected for player: {}", par2Str);

        if (HomeCommandContexts.getPreservePlayerHomesOnDeath().containsKey(this.getEntityName())) {
            var thePair = HomeCommandContexts.getPreservePlayerHomesOnDeath().get(this.getEntityName());

            this.mixin$backCoordinates = thePair.getSecond();

            if (mixin$homeCommandContext != thePair.getFirst()) {
                this.mixin$homeCommandContext.putAll(thePair.getFirst());
            }

            HomeCommandContexts.getPreservePlayerHomesOnDeath().remove(this.getEntityName());
        }
    }

    @Inject(method = "setDead", at = @At("HEAD"))
    public void inj$setDead(CallbackInfo ci) {
        this.mixin$backCoordinates = new Triple<>(
                this.posX,
                this.posY,
                this.posZ
        );

        HomeCommandContexts.getPreservePlayerHomesOnDeath().put(this.getEntityName(),
                new Pair<>(this.mixin$homeCommandContext, this.mixin$backCoordinates));
    }

    @Inject(method = "writeEntityToNBT", at = @At("RETURN"))
    public void inj$writeEntityToNBT(net.minecraft.NBTTagCompound nbt, CallbackInfo ci) {
        nbt.setFloat("regenerationAmount", this.mixin$regenerationAmount);

        Gson gson = new Gson();
        Map<String, List<Double>> tempMap = new HashMap<>();
        for (Map.Entry<String, Triple<Double, Double, Double>> entry : mixin$homeCommandContext.entrySet()) {
            Triple<Double, Double, Double> coords = entry.getValue();
            tempMap.put(entry.getKey(), List.of(coords.getFirst(), coords.getSecond(), coords.getThird()));
        }
        String json = gson.toJson(tempMap);
        nbt.setString("homeCommandContext", json);
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
    private Map<String, Triple<Double, Double, Double>> mixin$homeCommandContext = new HashMap<String, Triple<Double, Double, Double>>();

    @Unique
    private Triple<Double, Double, Double> mixin$backCoordinates = null;

    @Override
    public @NotNull List<String> getHomeNames() {
        return mixin$homeCommandContext.keySet().stream().toList();
    }

    @Override
    public Triple<Double, Double, Double> getHomeCoordinates(@NotNull String name) {
        return mixin$homeCommandContext.get(name);
    }

    @Override
    public void setHomeCoordinates(@NotNull String name, @NotNull Triple<Double, Double, Double> coordinates) {
        mixin$homeCommandContext.put(name, coordinates);
    }

    @Override
    public boolean deleteHome(@NotNull String name) {
        return mixin$homeCommandContext.remove(name) != null;
    }

    @Override
    public Triple<Double, Double, Double> getBackCoordinates() {
        return mixin$backCoordinates;
    }

    @Override
    public void setBackCoordinates(Triple<Double, Double, Double> coordinates) {
        mixin$backCoordinates = coordinates;
    }
}
