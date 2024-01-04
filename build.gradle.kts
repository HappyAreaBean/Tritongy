plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")
    id("maven-publish")
}

dependencies {
    implementation(project(":api", "shadow"))
}

allprojects {
    group = "net.fantasyrealms.tritongy"
    version = "3.0.3"

    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(8))
        withSourcesJar()
        withJavadocJar()
    }

    repositories {
        mavenCentral()

        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")
        maven("https://jitpack.io")
    }

    publishing {
        repositories {
            // See Gradle docs for how to provide credentials to PasswordCredentials
            // https://docs.gradle.org/current/samples/sample_publishing_credentials.html
            maven {
                name = "frsSnapshots"
                url = uri("https://repo.fantasyrealms.net/snapshots/")
                credentials(PasswordCredentials::class)
            }
            maven {
                name = "frsReleases"
                url = uri("https://repo.fantasyrealms.net/releases/")
                credentials(PasswordCredentials::class)
            }
        }
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