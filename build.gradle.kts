import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    idea
    kotlin("jvm") version "2.1.20"
    id("com.gradleup.shadow") version "9.0.2"
}

group = "me.mucloud"
val DEV = 1

repositories {
    maven("https://maven.aliyun.com/repository/public")

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/releases/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
}

java{
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks{
    withType<JavaCompile>{
        options.encoding = "UTF-8"
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>{
        compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    }

    shadowJar{
        archiveFileName = "${rootProject.name}_$version.$DEV.jar"
        dependencies{
            include(dependency("org.jetbrains.kotlin:.*"))
            include(dependency("org.jetbrains.kotlinx:.*"))
        }
        relocate("kotlin", "me.mucloud.mcplugin.MK.JoinMessage.libs.kotlin")
    }
}
