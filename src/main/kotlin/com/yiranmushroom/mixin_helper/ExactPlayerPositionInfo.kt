package com.yiranmushroom.mixin_helper

import net.minecraft.NBTTagCompound
import net.minecraft.NBTTagDouble
import net.minecraft.NBTTagFloat
import net.minecraft.NBTTagInt
import net.minecraft.NBTTagList

inline fun <reified T> NBTTagList.getIndexOptional(index: Int): T? {
    if (this.tagCount() < index) return null
    val tag = this.tagAt(index)
    return tag as? T
}

class ExactPlayerPositionInfo(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
    val yaw: Float = 0.0f,
    val pitch: Float = 0.0f,
    val dimensionId: Int = 0
) {
    constructor() : this(0.0, 0.0, 0.0, 0.0f, 0.0f, 0)

    fun toNBT(): NBTTagCompound {
        val nbt = NBTTagCompound()

        nbt.setDouble("x", x)
        nbt.setDouble("y", y)
        nbt.setDouble("z", z)
        nbt.setFloat("yaw", yaw)
        nbt.setFloat("pitch", pitch)
        nbt.setInteger("dimensionId", dimensionId)

        return nbt
    }

    companion object {
        @JvmStatic
        fun fromNBT(nbt: NBTTagCompound): ExactPlayerPositionInfo {
            return try {
                ExactPlayerPositionInfo(
                    x = nbt.getDouble("x"),
                    y = nbt.getDouble("y"),
                    z = nbt.getDouble("z"),
                    yaw = nbt.getFloat("yaw"),
                    pitch = nbt.getFloat("pitch"),
                    dimensionId = nbt.getInteger("dimensionId")
                )
            } catch (ignored: Exception) {
                ExactPlayerPositionInfo()
            }
        }
    }
}
