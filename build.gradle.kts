import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileReader

plugins {
    java
    idea
    kotlin("jvm") version "2.1.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
    testImplementation("junit:junit:4.13.1")

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

    test{
        useJUnitPlatform()
    }

    jar{
        archiveFileName.set("MK-JoinMessage-KotlinEdition_${project.version}.jar")
    }

    shadowJar{

        archiveFileName = "${rootProject.name}_$version.$DEV.jar"

        dependencies{
            include(dependency("org.jetbrains.kotlin:.*"))
            include(dependency("org.jetbrains.kotlinx:.*"))
        }
        relocate("kotlin", "me.mucloud.mcplugin.MK.JoinMessage.libs.kotlin")
        relocate("com.zaxxer.hikari", "me.mucloud.mcplugin.MK.JoinMessage.libs.com.zaxxer.hikari")
    }

}
