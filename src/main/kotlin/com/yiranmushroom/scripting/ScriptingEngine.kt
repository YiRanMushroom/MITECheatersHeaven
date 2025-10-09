package com.yiranmushroom.scripting

import com.yiranmushroom.MITECheatersHeaven.Companion.LOGGER
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.JvmDependency
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

/**
 * A robust manager for our Kotlin scripting engine.
 * This version initializes asynchronously and loads scripts in parallel to improve performance.
 */
class ScriptingEngine private constructor() {

    // Use ConcurrentHashMap for thread-safe access from multiple script-loading threads.
    val functionMap: ConcurrentHashMap<String, Any> = ConcurrentHashMap()

    init {
        // Find or create the "scripts" directory. This logic is unchanged.
        val scriptDir = File("scripts")
        if (!scriptDir.exists()) {
            scriptDir.mkdirs()
            LOGGER.info("Created scripts directory at: ${scriptDir.absolutePath}")
        } else {
            LOGGER.info("Scripts directory found at: ${scriptDir.absolutePath}")
            loadScriptsFrom(scriptDir)
        }

        LOGGER.info("Total functions loaded: ${functionMap.size}")
    }

    private fun loadScriptsFrom(scriptDir: File) {
        val classpath = System.getProperty("java.class.path")
        val classpathFiles = classpath.split(File.pathSeparator).map { File(it) }

        val host = BasicJvmScriptingHost()
        val compilationConfiguration = ScriptCompilationConfiguration {
            dependencies(JvmDependency(classpathFiles))
        }

        val scriptFiles = scriptDir.walkTopDown().filter { it.isFile && it.extension == "gs" }

        // --- PARALLEL LOADING LOGIC ---
        // Create a thread pool with a thread for each available CPU core.
        val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

        try {
            // Submit each script to be loaded and compiled on a separate thread.
            for (scriptFile in scriptFiles) {
                executor.submit {
                    try {
                        LOGGER.info("Loading script: ${scriptFile.name} on thread ${Thread.currentThread().name}...")
                        val scriptSource = scriptFile.readText().toScriptSource()
                        val result = host.eval(scriptSource, compilationConfiguration, null)

                        result.reports.forEach {
                            if (it.severity >= ScriptDiagnostic.Severity.WARNING) {
                                LOGGER.warn("Diagnostic from script '${scriptFile.name}': ${it.message} at ${it.location}")
                            }
                        }

                        if (result is ResultWithDiagnostics.Success) {
                            val returnValueHolder = result.value.returnValue
                            if (returnValueHolder is ResultValue.Value) {
                                val actualValue = returnValueHolder.value
                                if (actualValue is Map<*, *>) {
                                    for ((key, value) in actualValue) {
                                        if (key is String && value != null) {
                                            // functionMap is a ConcurrentHashMap, so this is thread-safe.
                                            functionMap[key] = value
                                            LOGGER.info(" -> Loaded function '$key' from script: ${scriptFile.name}")
                                        }
                                    }
                                } else {
                                    LOGGER.warn("Script returned a value, but it was not a Map: ${scriptFile.name}")
                                }
                            } else {
                                LOGGER.warn("Script did not return a value (Result was Unit): ${scriptFile.name}")
                            }
                        } else {
                            LOGGER.error("Evaluation failed for script: ${scriptFile.name}")
                        }
                    } catch (e: Exception) {
                        LOGGER.error("Critical error evaluating script: ${scriptFile.name}", e)
                    }
                }
            }
        } finally {
            // Important: Shut down the executor and wait for all tasks to complete.
            executor.shutdown()
            try {
                // Wait for a reasonable amount of time for all scripts to finish loading.
                executor.awaitTermination(5, TimeUnit.MINUTES)
            } catch (e: InterruptedException) {
                LOGGER.error("Script loading was interrupted.", e)
            }
        }
    }

    inline fun <reified T> getFunction(name: String, orDefault: T): T {
        val func = functionMap[name]
        return if (func is T) {
            func
        } else {
            orDefault
        }
    }

    companion object {
        @JvmStatic
        @Volatile
        private var _instance: ScriptingEngine? = null
        private val latch = CountDownLatch(1)

        @JvmStatic
        val instance: ScriptingEngine
            get() {
                latch.await()
                return _instance!!
            }

        @JvmStatic
        fun Init() {
            Thread {
                _instance = ScriptingEngine()
                latch.countDown()
                LOGGER.info("ScriptingEngine initialized asynchronously on a background thread.")
            }.start()
        }
    }
}

