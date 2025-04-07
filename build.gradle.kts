plugins {
    id("java")
}

group = "me.joao"
version = "0.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    //maven("https://oss.sonatype.org/content/repositories/snapshots")
    //maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT") // The Spigot API with no shadowing. Requires the OSS repo.
    compileOnly("org.spigotmc:spigot:1.20.2-R0.1-SNAPSHOT") // The full Spigot server with no shadowing. Requires mavenLocal.
}

tasks.test {
    useJUnitPlatform()
}