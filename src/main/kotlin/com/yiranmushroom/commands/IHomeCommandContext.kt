package com.yiranmushroom.commands


interface IHomeCommandContext {
    fun getHomeNames(): List<String>
    fun getHomeCoordinates(homeName: String): Triple<Double, Double, Double>?
    fun setHomeCoordinates(homeName: String, coordinates: Triple<Double, Double, Double>)
    fun deleteHome(homeName: String): Boolean
    fun getBackCoordinates(): Triple<Double, Double, Double>?
    fun setBackCoordinates(coordinates: Triple<Double, Double, Double>)
}