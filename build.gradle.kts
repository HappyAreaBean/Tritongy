plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
}

dependencies {
    implementation(project(":api", "shadow"))
}

allprojects {
    group = "net.islandearth.languagy"
    version = "2.0.5"

    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "java")

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(8))
    }

    repositories {
        mavenCentral()

        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")
        maven("https://jitpack.io")
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveClassifier.set("")
    }
}