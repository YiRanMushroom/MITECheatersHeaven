package com.yiranmushroom.mixin_helper

import com.yiranmushroom.mixin_helper.MaterialQualityScripting.durabilityModifierField
import net.minecraft.EnumQuality
import java.lang.reflect.Field

object MaterialQualityScripting {
    val durabilityModifierField: Field by lazy {
        EnumQuality::class.java.getDeclaredField("durability_modifier")
            .apply { isAccessible = true }
    }
}

fun EnumQuality.modifyDurabilityModifierOfQualityUnsafe(newModifier: Float) {
    durabilityModifierField.setFloat(this, newModifier)
}
