repositories {
    mavenCentral()

    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://jitpack.io")
}

dependencies {
    implementation("commons-codec:commons-codec:1.15")
    implementation("commons-io:commons-io:2.11.0")

    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:23.0.0")
}

tasks {
    shadowJar {
        relocate("org.apache.commons.codec", "net.islandearth.languagy.commons.codec")
        relocate("org.apache.commons.io", "net.islandearth.languagy.commons.io")
    }
}