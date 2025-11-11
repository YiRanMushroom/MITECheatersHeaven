package com.yiranmushroom.commands

import com.yiranmushroom.mixin_helper.ExactPlayerPositionInfo


interface IHomeCommandContext {
    fun getHomeNames(): List<String>
    fun getHomeExactPosition(homeName: String): ExactPlayerPositionInfo?
    fun setHomeExactPosition(homeName: String, position: ExactPlayerPositionInfo)
    fun deleteHome(homeName: String): Boolean
    fun getBackExactPosition(): ExactPlayerPositionInfo?
    fun setBackExactPosition(position: ExactPlayerPositionInfo)

    fun getCurrentExactPositionInfo(): ExactPlayerPositionInfo
    fun setPlayerToExactPositionInfo(positionInfo: ExactPlayerPositionInfo)
}