plugins {
    id("fabric-loom") version "1.17.13"
    id("maven-publish")
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

val archivesBaseName = project.property("archives_base_name") as String
val minecraftVersion = project.property("minecraft_version") as String
val yarnMappings = project.property("yarn_mappings") as String
val loaderVersion = project.property("loader_version") as String
val fabricVersion = project.property("fabric_version") as String
val javaVersion = (project.property("java_version") as String).toInt()

base {
    archivesName.set(archivesBaseName)
}

repositories {
    maven("https://maven.fabricmc.net/") { name = "Fabric" }
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", minecraftVersion)
    inputs.property("loader_version", loaderVersion)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to minecraftVersion,
            "loader_version" to loaderVersion
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(javaVersion)
}

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
    withSourcesJar()
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${archivesBaseName}" }
    }
}

// Produce a jar named LightClient-1.21.11.jar regardless of internal version.
tasks.remapJar {
    archiveBaseName.set(archivesBaseName)
    archiveVersion.set(minecraftVersion)
    archiveClassifier.set("")
}
