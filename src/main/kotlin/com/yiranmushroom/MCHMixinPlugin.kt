package com.yiranmushroom

import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class MCHMixinPlugin : IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String) {
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        println("Considering mixin $mixinClassName for target $targetClassName")
        return true
    }

    override fun acceptTargets(
        myTargets: Set<String>,
        otherTargets: Set<String>
    ) {
    }

    override fun getMixins(): List<String> {
        return listOf(
        )
    }

    override fun preApply(
        p0: String,
        p1: ClassNode,
        p2: String,
        p3: IMixinInfo
    ) {
    }

    override fun postApply(
        p0: String,
        p1: ClassNode,
        p2: String,
        p3: IMixinInfo
    ) {
        println("IMixinInfo: $p3")
    }

    companion object {
        val CancelledMixins = mutableMapOf<String, String>(

        )

        val AdditionalMixins = mutableMapOf<String, (IMixinInfo) -> List<String>>()
    }
}