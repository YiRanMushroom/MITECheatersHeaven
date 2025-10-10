package com.yiranmushroom.scripting

import java.util.concurrent.ConcurrentHashMap

object Events {
    private var onScriptsLoadedSet: MutableSet<() -> Unit> = ConcurrentHashMap.newKeySet()
    fun onScriptsLoaded(action: () -> Unit) {
        onScriptsLoadedSet.add(action)
    }

    fun triggerScriptsLoaded() {
        onScriptsLoadedSet.forEach { it() }
    }
}