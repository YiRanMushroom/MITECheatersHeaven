package com.yiranmushroom.mixin_helper

import com.yiranmushroom.scripting.ScriptingEngine
import net.minecraft.Block
import net.minecraft.Entity
import net.minecraft.EntityPlayer
import net.minecraft.EnumEntityReachContext

object EntityPlayerScripting {
    @JvmStatic
    var getReach1: (EntityPlayer, EnumEntityReachContext, Entity, Float) -> Float =
        { p: EntityPlayer, c: EnumEntityReachContext, e: Entity, original: Float -> original }
//    var getReach1: (EntityPlayer, EnumEntityReachContext, Entity, Float) -> Float =
//        { player: EntityPlayer, context: EnumEntityReachContext, entity: Entity, original: Float ->
//            // try load from script
//            val engine = ScriptingEngine.instance
//
//            var function = engine.getFunction(
//                "getReach1"
//            ) { p: EntityPlayer, c: EnumEntityReachContext, e: Entity, original: Float -> original }
//
//            getReach1 = function
//            function(player, context, entity, original)
//        }

    @JvmStatic
    var getReach2: (EntityPlayer, Block, Int, Float) -> Float =
        { p: EntityPlayer, b: Block, m: Int, original: Float -> original }
//    var getReach2: (EntityPlayer, Block, Int, Float) -> Float =
//        { player: EntityPlayer, block: Block, metadata: Int, original: Float ->
//            // try load from script
//            val engine = ScriptingEngine.instance
//
//            var function = engine.getFunction(
//                "getReach2"
//            ) { p: EntityPlayer, b: Block, m: Int, original: Float -> original }
//
//            getReach2 = function
//            function(player, block, metadata, original)
//        }

    @JvmStatic
    var getRelativeBlockHardness: (EntityPlayer, Int, Int, Int, Boolean, Float) -> Float =
        { p: EntityPlayer, x: Int, y: Int, z: Int, isRemote: Boolean, original: Float -> original }
//    var getRelativeBlockHardness: (EntityPlayer, Int, Int, Int, Boolean, Float) -> Float =
//        { player: EntityPlayer, x: Int, y: Int, z: Int, isRemote: Boolean, original: Float ->
//            // try load from script
//            val engine = ScriptingEngine.instance
//
//            var function = engine.getFunction(
//                "getRelativeBlockHardness"
//            ) { p: EntityPlayer, x: Int, y: Int, z: Int, isRemote: Boolean, original: Float -> original }
//
//            getRelativeBlockHardness = function
//            function(player, x, y, z, isRemote, original)
//        }
}