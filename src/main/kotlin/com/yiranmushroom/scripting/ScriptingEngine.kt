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
 * This version is optimized to prevent silent failures by enhancing error catching and logging.
 */
class ScriptingEngine private constructor() {

    // Use ConcurrentHashMap for thread-safe access from multiple script-loading threads.
//    val functionMap: ConcurrentHashMap<String, Any> = ConcurrentHashMap()

    init {
        val scriptDir = File("scripts")
        if (!scriptDir.exists()) {
            scriptDir.mkdirs()
            LOGGER.info("Created scripts directory at: ${scriptDir.absolutePath}")
        } else {
            LOGGER.info("Scripts directory found at: ${scriptDir.absolutePath}")
            loadScriptsFrom(scriptDir)
        }
        LOGGER.info("ScriptingEngine initialized.")

        Events.triggerScriptsLoaded()
    }

    fun preprocessWithImports(
        file: File,
        visited: MutableSet<String> = mutableSetOf(),
        importedFiles: MutableSet<String> = mutableSetOf()
    ): String {
        val canonicalPath = file.canonicalPath
        if (!visited.add(canonicalPath)) throw IllegalStateException("Circular import detected: $canonicalPath")
        if (!importedFiles.add(canonicalPath)) {
            visited.remove(canonicalPath)
            return ""
        }
        val importLines = mutableSetOf<String>()
        val contentLines = mutableListOf<String>()
        val importRegex = Regex("""^\s*@import\s*"(.*?)"\s*""")
        file.readLines().forEach { line ->
            val match = importRegex.find(line)
            if (match != null) {
                val importPath = match.groupValues[1]
                val importFile = File(
                    if (File(importPath).isAbsolute) importPath
                    else file.parentFile.resolve(importPath).path
                )
                if (!importFile.exists()) throw IllegalArgumentException("No such import file: $importPath in ${file.path}")
                val imported = preprocessWithImports(importFile, visited, importedFiles)
                imported.lines().forEach {
                    if (it.trim().startsWith("import ")) importLines.add(it.trim())
                    else contentLines.add(it)
                }
            } else {
                if (line.trim().startsWith("import ")) importLines.add(line.trim())
                else contentLines.add(line)
            }
        }
        visited.remove(canonicalPath)
        return (importLines.joinToString("\n") + "\n" + contentLines.joinToString("\n"))
    }


    private fun loadScriptsFrom(scriptDir: File) {
        val classpath = System.getProperty("java.class.path")
        val classpathFiles = classpath.split(File.pathSeparator).map { File(it) }
        val allDeps = classpathFiles.filter { it.exists() }
        val compilationConfiguration = ScriptCompilationConfiguration {
            dependencies(JvmDependency(allDeps))
        }
        val host = BasicJvmScriptingHost()

        val scriptFiles = scriptDir.walkTopDown()
            .filter { it.isFile && (it.extension == "kts" || it.extension == "inc") }
            .filter { it.extension == "kts" }
            .toList()

        val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        try {
            for (scriptFile in scriptFiles) {
                executor.submit {
                    try {
                        val scriptText = preprocessWithImports(scriptFile)
                        val scriptSource = scriptText.toScriptSource()
                        val result = host.eval(scriptSource, compilationConfiguration, null)
                        var hasErrors = false
                        if (result.reports.isNotEmpty()) {
                            result.reports.forEach {
                                val severity = it.severity
                                val message = "Diagnostic from script '${scriptFile.name}': ${severity} - ${it.message} at ${it.location}"
                                when {
                                    severity >= ScriptDiagnostic.Severity.ERROR -> {
                                        LOGGER.error(message)
                                        hasErrors = true
                                    }
                                    severity >= ScriptDiagnostic.Severity.WARNING -> LOGGER.warn(message)
                                    else -> LOGGER.info(message)
                                }
                            }
                        }

                        if (hasErrors) {
                            // print the script content for easier debugging
                            LOGGER.error("Script content of '${scriptFile.name}':\n${scriptText}")
                        }

                        if (!hasErrors && result is ResultWithDiagnostics.Success) {
                            val returnValueHolder = result.value.returnValue
                            if (returnValueHolder is ResultValue.Value) {
                                val actualValue = returnValueHolder.value
                                LOGGER.info("Successfully evaluated script: ${scriptFile.name}, returned value: $actualValue")
                            }
                        }
                    } catch (t: Throwable) {
                        LOGGER.error("Critical throwable caught while evaluating script: ${scriptFile.name}", t)
                    }
                }
            }
        } finally {
            executor.shutdown()
            executor.awaitTermination(5, TimeUnit.MINUTES)
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

