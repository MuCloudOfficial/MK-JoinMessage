plugins {
    kotlin("jvm") version "1.9.21"
}

group = "me.mucloud"
version = "SakuraOcean"

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")

    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("junit:junit:4.13.1")

    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.2")
}

java{
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks{

    withType<JavaCompile>{
        options.encoding = "UTF-8"
    }

    test{
        useJUnitPlatform()
    }

    jar{

    }

}

