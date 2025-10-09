import kotlinx.metadata.internal.metadata.deserialization.VersionRequirementTable.Companion.create
import net.fabricmc.loom.api.ModSettings

plugins {
    id("fml-loom") version "0.1.local"
    `maven-publish`
    kotlin("jvm") version "1.9.23" // Added for Kotlin language support
}

// Define properties from gradle.properties for type-safe access
val mod_version: String by project
val maven_group: String by project
//var archives_base_name: String by project
val loader_version: String by project
val user_name: String by project
val window_width: String by project
val window_height: String by project
val minecraft_version: String by project
val rusted_iron_core_version: String by project
val mod_id: String by project

val archives_base_name = mod_id // Set archives_base_name to mod_id

version = mod_version
group = maven_group

base {
    archivesName.set(archives_base_name)
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
    mavenLocal() // for local files
    maven {
        name = "Spongepowered"
        url = uri("https://repo.spongepowered.org/repository/maven-public/")
    }//Dependency library(FastUtil, Gson, etc) download source
}

loom {
    accessWidenerPath.set(file("src/main/resources/$mod_id.accesswidener"))
    mergedMinecraftJar()
    //latest fishmodloader https://github.com/MinecraftIsTooEasy/FishModLoader/releases/latest
    setFML(File("libs/FishModLoader-v$loader_version.jar"))
    mods {
        val modid by creating {
            sourceSet(sourceSets.main.get())
//            sourceSet(sourceSets.client.get())
//            sourceSet(sourceSets.server.get())
            //For modules in the ’src‘ directory, only the main module is required by default
        }
    }

}

tasks.named<JavaExec>("runClient") {
    args = listOf("--username", user_name, "--session", "a.a.a.a", "--width", window_width, "--height", window_height)
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings(loom.fmlMCPMappings())
    implementation(files(loom.getFML().toPath()))
    implementation("it.unimi.dsi:fastutil:8.5.12")
    implementation("com.google.code.gson:gson:2.11.0")

    // Rusted_Iron_Core API. This is technically optional, but you probably want it anyway.
    implementation(files("libs/RustedIronCore-$rusted_iron_core_version.jar"))
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
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Jar> {
    inputs.property("archivesName", base.archivesName.get())

    from("LICENSE") {
        rename { "${it}_${inputs.properties["archivesName"]}"}
    }
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = archives_base_name
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}
