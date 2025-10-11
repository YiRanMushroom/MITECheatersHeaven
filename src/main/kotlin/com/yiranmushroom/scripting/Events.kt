package com.yiranmushroom.scripting

import com.yiranmushroom.MITECheatersHeaven
import net.xiaoyu233.fml.reload.event.ItemRegistryEvent
import java.util.concurrent.ConcurrentHashMap

object Events {
    private var onScriptsLoadedSet: MutableSet<Pair<String, () -> Unit>> = ConcurrentHashMap.newKeySet()
    fun onScriptsLoaded(hint: String, action: () -> Unit) {
        onScriptsLoadedSet.add(Pair(hint, action))
    }

    fun onScriptsLoaded(action: () -> Unit) {
        onScriptsLoadedSet.add(Pair("Unlabeled", action))
    }

    fun triggerScriptsLoaded() {
        onScriptsLoadedSet.forEach {
            try {
                MITECheatersHeaven.LOGGER.info("Triggering script loaded event: ${it.first}")
                it.second()
            } catch (e: Exception) {
                MITECheatersHeaven.LOGGER.error("Error during script loaded event: ${it.first}", e)
            }
        }

    }

    private var onModItemRegisterSet: MutableSet<Pair<String, (ItemRegistryEvent) -> Unit>> =
        ConcurrentHashMap.newKeySet()

    fun onModItemRegister(hint: String, action: (ItemRegistryEvent) -> Unit) {
        onModItemRegisterSet.add(Pair(hint, action))
    }

    fun onModItemUnregister(action: (ItemRegistryEvent) -> Unit) {
        onModItemRegisterSet.add(Pair("Unlabeled", action))
    }

    fun triggerModItemRegister(event: ItemRegistryEvent) {
        MITECheatersHeaven.LOGGER.info("Triggering mod item registration events for ${onModItemRegisterSet.size} listeners.")
        onModItemRegisterSet.forEach {
            try {
                MITECheatersHeaven.LOGGER.info("Triggering mod item register event: ${it.first}")
                it.second(event)
            } catch (e: Exception) {
                MITECheatersHeaven.LOGGER.error("Error during mod item register event: ${it.first}", e)
            }
        }
    }

    private var onPostInitializationSet: MutableSet<Pair<String, () -> Unit>> = ConcurrentHashMap.newKeySet()
    fun onPostInitialization(hint: String, action: () -> Unit) {
        onPostInitializationSet.add(Pair(hint, action))
    }

    fun onPostInitialization(action: () -> Unit) {
        onPostInitializationSet.add(Pair("Unlabeled", action))
    }

    @JvmStatic
    fun triggerPostInitialization() {
        onPostInitializationSet.forEach {
            try {
                MITECheatersHeaven.LOGGER.info("Triggering post-initialization event: ${it.first}")
                it.second()
            } catch (e: Exception) {
                MITECheatersHeaven.LOGGER.error("Error during post-initialization event: ${it.first}", e)
            }
        }

    }
}