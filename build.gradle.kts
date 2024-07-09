import java.io.FileReader

plugins {
    java
    idea
    kotlin("jvm") version "1.9.24"
    id("com.github.johnrengelman.shadow") version "7.1.1"
}

val ymlreader = FileReader(File(projectDir, "src/main/resources/plugin.yml")).readLines()

group = "me.mucloud"
private lateinit var versionCN: String
private var DEV: Int = 0
private lateinit var authors: List<String>
private lateinit var versionCNV: String
private lateinit var versionV: String

ymlreader.forEach { s ->
    val ss = s.split(": ")
    when(ss[0]){
        "version" -> version = ss[1]
        "versionCN" -> versionCN = ss[1]
        "internalVersion" -> DEV = ss[1].toInt()
        "authors" -> authors = ss[1].substring(1).dropLast(1).trim().split(",")
        "versionCNV" -> versionCNV = ss[1]
        "versionV" -> versionV = ss[1]
    }
}

println("""
==================================
= MK-JoinMessage     Build Info  =
==================================
| VERSION >>> $versionCN($version) DEV.$DEV
| AUTHORS >>> ${authors.toString().substring(1).dropLast(1)}
==================================
=      MADE IN SAKURA OCEAN      =
==================================
""".trimIndent())

repositories {
    maven("https://maven.aliyun.com/repository/public")

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("junit:junit:4.13.1")

    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.2")

    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.xerial:sqlite-jdbc:3.45.3.0")
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
        archiveFileName.set("MK-JoinMessage-KotlinEdition_${project.version}.jar")
    }

}
