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
    setFML(File("libs/FishModLoader-v$loader_version.jar"))
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
    implementation(files(loom.getFML().toPath()))
    implementation("it.unimi.dsi:fastutil:8.5.12")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation(files("libs/RustedIronCore-$rusted_iron_core_version.jar"))
    implementation(files("libs/ManyLib-$manylib_version.jar"))
    implementation(files("libs/ModMenu-$modmenu_version.jar"))
    implementation(files("libs/Extreme-0.1.6.10.jar"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-scripting-common:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host:$kotlin_version")
//    implementation("org.jetbrains.kotlin:kotlin-script-runtime:$kotlin_version")

//    implementation("org.jetbrains.kotlin:kotlin-scripting-jsr223:$kotlin_version")
//    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlin_version")
//    implementation("org.jetbrains.kotlin:kotlin-scripting-dependencies:$kotlin_version")
//    implementation("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:$kotlin_version")
//    implementation("org.jetbrains.kotlin:kotlin-scripting-compiler-impl:$kotlin_version")
//    implementation("org.jetbrains.kotlin:kotlin-compiler:$kotlin_version")

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

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
//    archiveClassifier.set("")
//    mergeServiceFiles()
//
//    from(zipTree(file("libs/trove.jar")))
//
//    dependencies {
//        include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
//        include(dependency("org.jetbrains.kotlin:kotlin-reflect"))
//        include(dependency("org.jetbrains.kotlin:kotlin-scripting-common"))
//        include(dependency("org.jetbrains.kotlin:kotlin-scripting-jvm"))
//        include(dependency("org.jetbrains.kotlin:kotlin-scripting-jvm-host"))
////        include(dependency("org.jetbrains.kotlin:kotlin-script-runtime"))
//
////        include(dependency("org.jetbrains.kotlin:kotlin-scripting-jsr223"))
////        include(dependency("org.jetbrains.kotlin:kotlin-compiler-embeddable"))
////        include(dependency("org.jetbrains.kotlin:kotlin-scripting-dependencies"))
////        include(dependency("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable"))
////        include(dependency("org.jetbrains.kotlin:kotlin-scripting-compiler-impl"))
////        include(dependency("org.jetbrains.kotlin:kotlin-compiler"))
//
//        include(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core"))
//    }
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
