import java.nio.charset.StandardCharsets

plugins {
    id("java")
    id("maven-publish")

    // External Plugins
    alias(libs.plugins.shadow)
    alias(libs.plugins.lombok.java)

    // Kotlin Plugins
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.lombok.kotlin)
}

group = "me.joao"
version = "0.0.0-SNAPSHOT"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven ("https://repo.dmulloy2.net/repository/public/")
    mavenCentral()
}

dependencies {
    // Annotations
    compileOnly(libs.annotations)
    annotationProcessor(libs.annotations)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Extra Libraries
    implementation(libs.bundles.adventure)

    // Kernel
    compileOnly(libs.bundles.spigot)

    // Plugins Compatibility
    compileOnly(libs.luckperms)
    compileOnly(libs.protocolLib)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}

tasks.withType<ProcessResources> {
    filteringCharset = StandardCharsets.UTF_8.name()
}

tasks {
    val copyJarToServer by registering(Copy::class) {
        dependsOn(shadowJar)
        from(shadowJar)
        into("C:/Users/jpqv0/MinecraftServer/plugins")
    }

    shadowJar {
        archiveFileName.set("${project.name}.jar")
    }

    build {
        dependsOn(copyJarToServer)
    }

    processResources {
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "group" to project.group,
            "mainClass" to project.properties["mainClass"],
            "authors" to project.properties["authors"],
            "description" to project.properties["description"],
            "website" to project.properties["website"],
            "depend" to project.properties["depend"],
            "softDepend" to project.properties["softDepend"],
            "loadBefore" to project.properties["loadBefore"]
        )
        inputs.properties(props)

        filesMatching("plugin.yml") {
            expand(props)
        }
        filesMatching("bungee.yml") {
            expand(props)
        }
        filesMatching("module.yml") {
            expand(props)
        }
    }
}