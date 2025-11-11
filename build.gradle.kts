import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val mod_version: String by project
val maven_group: String by project
val loader_version: String by project
val user_name: String by project
val window_width: String by project
val window_height: String by project
val minecraft_version: String by project
val rusted_iron_core_version: String by project
val mod_id: String by project
val manylib_version: String by project
val modmenu_version: String by project
val kotlin_version: String by project

plugins {
    id("fml-loom") version "0.1.local"
    kotlin("jvm") version "2.2.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    `maven-publish`
}

version = mod_version
group = maven_group
base { archivesName.set(mod_id) }

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

loom {
    accessWidenerPath.set(file("src/main/resources/$mod_id.accesswidener"))
    mergedMinecraftJar()
    fml = File("libs/FishModLoader-v$loader_version.jar")
    mods {
        create(mod_id) { sourceSet(sourceSets.main.get()) }
    }
}

tasks.named<JavaExec>("runClient") {
    args = listOf("--username", user_name, "--session", "a.a.a.a", "--width", window_width, "--height", window_height)
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.fmlMCPMappings())
    implementation(files(loom.fml.toPath()))
    implementation("it.unimi.dsi:fastutil:8.5.12")
    implementation("com.google.code.gson:gson:2.11.0")
    compileOnly(files("libs/RustedIronCore-$rusted_iron_core_version.jar"))
    compileOnly(files("libs/ManyLib-$manylib_version.jar"))
    compileOnly(files("libs/ModMenu-$modmenu_version.jar"))
    compileOnly(files("libs/MITE-ITE-2.0.18.jar"))
    compileOnly(files("libs/Extreme-0.1.6.10.jar"))
    compileOnly(files("libs/conditional-mixin-0.6.4.jar"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-scripting-common:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host:$kotlin_version")

    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

tasks.withType<ProcessResources> {
    inputs.property("version", project.version)
    filesMatching("fml.mod.json") {
        expand(mapOf("version" to project.version))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Jar> {
    inputs.property("archivesName", base.archivesName.get())
    from("LICENSE") { rename { "${it}_${inputs.properties["archivesName"]}" } }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    mergeServiceFiles()
    configurations = listOf(project.configurations.runtimeClasspath.get())
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = mod_id
            from(components["java"])
        }
    }
}
