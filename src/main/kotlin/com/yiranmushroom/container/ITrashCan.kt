package com.yiranmushroom.container

import net.minecraft.IInventory

interface IOpenTrashCan {
    fun openTrashCan()
}

interface IClearTrashCan {
    fun clearTrashCan()
}

interface IGetTrashCanInventory {
    fun getTrashCanInventory(): IInventory
}