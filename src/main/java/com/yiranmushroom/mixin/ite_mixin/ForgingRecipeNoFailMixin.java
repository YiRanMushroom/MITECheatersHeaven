package com.yiranmushroom.mixin.ite_mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.xiaoyu233.mitemod.miteite.item.recipe.ForgingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition(value = "mite_ite"))
@Mixin(ForgingRecipe.class)
public class ForgingRecipeNoFailMixin {
    @ModifyExpressionValue(method = "getChanceOfFailure", at = @At(value = "FIELD", target = "Lnet/xiaoyu233/mitemod/miteite/item/recipe/ForgingRecipe;chanceOfFailure:I"))
    private int ith$wrapNoFail(int original) {
        return 0;
    }
}
