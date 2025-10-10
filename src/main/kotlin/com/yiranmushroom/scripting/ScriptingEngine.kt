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
import kotlin.script.experimental.jvm.JvmDependencyFromClassLoader
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

/**
 * A robust manager for our Kotlin scripting engine.
 * This version is optimized to prevent silent failures by enhancing error catching and logging.
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
            // for all .kts files in the scripts directory, load them
            loadScriptsFrom(scriptDir)
        }

        LOGGER.info("Total functions loaded: ${functionMap.size}")
    }

    private fun loadScriptsFrom(scriptDir: File) {
        val classpath = System.getProperty("java.class.path")
        val classpathFiles = classpath.split(File.pathSeparator).map { File(it) }

        val classLoaderJars = generateSequence(ClassLoader.getSystemClassLoader()) { it.parent }
            .filterIsInstance<java.net.URLClassLoader>()
            .flatMap { it.urLs.asSequence() }
            .mapNotNull { runCatching { File(it.toURI()) }.getOrNull() }
            .filter { it.exists() && it.extension == "jar" }
            .toList()

        fun findAllJars(dir: File): List<File> =
            if (dir.exists() && dir.isDirectory)
                dir.walkTopDown().filter { it.isFile && it.extension == "jar" }.toList()
            else emptyList()

        val modsJars = findAllJars(File("mods"))
        val fmlJars = File(".").walkTopDown()
            .filter { it.isDirectory && it.name.endsWith(".fml") }
            .flatMap { findAllJars(it) }
            .toList()

        val allDeps = (classpathFiles + classLoaderJars + modsJars + fmlJars).distinct().filter { it.exists() }

        LOGGER.info("Collected ${allDeps.size} dependencies for script compilation. They are: ${allDeps.joinToString(", ") { it.name }}")

        val compilationConfiguration = ScriptCompilationConfiguration {
            dependencies(JvmDependency(allDeps))
        }

        val host = BasicJvmScriptingHost()

        LOGGER.info("BasicJvmScriptingHost and compilation configuration created successfully.")

        val scriptFiles = scriptDir.walkTopDown().filter { it.isFile && it.extension == "kts" }
        LOGGER.info("Found ${scriptFiles.count()} script files to load: ${scriptFiles.map { it.name }.toList()}")

        // --- PARALLEL LOADING LOGIC ---
        // Create a thread pool with a thread for each available CPU core.
        val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

        try {
            // Submit each script to be loaded and compiled on a separate thread.
            for (scriptFile in scriptFiles) {
                executor.submit {
                    try {
                        LOGGER.info("Thread ${Thread.currentThread().name} started processing script: ${scriptFile.name}")

                        val scriptText = scriptFile.readText()
                        val scriptSource = scriptText.toScriptSource()

                        LOGGER.info(" -> Script '${scriptFile.name}' read successfully, length: ${scriptText.length}. Starting evaluation...")

                        // Use the host to evaluate the script with our new configuration.
                        val result = host.eval(scriptSource, compilationConfiguration, null)
                        LOGGER.info(" -> Script '${scriptFile.name}' evaluation finished.")

                        var hasErrors = false
                        if (result.reports.isNotEmpty()) {
                            result.reports.forEach {
                                val severity = it.severity
                                val message =
                                    "Diagnostic from script '${scriptFile.name}': ${severity} - ${it.message} at ${it.location}"
                                when {
                                    severity >= ScriptDiagnostic.Severity.ERROR -> {
                                        LOGGER.error(message)
                                        hasErrors = true
                                    }

                                    severity >= ScriptDiagnostic.Severity.WARNING -> LOGGER.warn(message)
                                    else -> LOGGER.info(message)
                                }
                            }
                        } else {
                            LOGGER.info(" -> Script '${scriptFile.name}' evaluation produced no diagnostic reports.")
                        }

                        if (!hasErrors && result is ResultWithDiagnostics.Success) {
                            val returnValueHolder = result.value.returnValue
                            if (returnValueHolder is ResultValue.Value) {
                                val actualValue = returnValueHolder.value
                                if (actualValue is Map<*, *>) {
                                    for ((key, value) in actualValue) {
                                        if (key is String && value != null) {
                                            functionMap[key] = value
                                            LOGGER.info(" -> Loaded function '$key' from script: ${scriptFile.name}")
                                        }
                                    }
                                } else {
                                    LOGGER.warn("Script '${scriptFile.name}' returned a value, but it was not a Map. Actual type: ${actualValue?.javaClass?.name}")
                                }
                            } else {
                                LOGGER.warn("Script '${scriptFile.name}' did not return a value (Result was Unit, an error, or another non-value type: ${returnValueHolder.javaClass.simpleName}).")
                            }
                        } else {
                            LOGGER.error("Evaluation failed for script '${scriptFile.name}' due to errors in diagnostic reports or because the result was not 'Success'.")
                        }
                    } catch (t: Throwable) {
                        LOGGER.error("Critical throwable caught while evaluating script: ${scriptFile.name}", t)
                    } finally {
                        LOGGER.info("Thread ${Thread.currentThread().name} finished processing script: ${scriptFile.name}")
                    }
                }
            }
        } finally {
            LOGGER.info("All script loading tasks submitted to executor. Shutting down...")
            executor.shutdown()
            try {
                // Wait for a reasonable amount of time for all scripts to finish loading.
                executor.awaitTermination(5, TimeUnit.MINUTES)
                LOGGER.info("Executor has terminated successfully.")
            } catch (e: InterruptedException) {
                LOGGER.error("Script loading was interrupted while waiting for executor termination.", e)
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

