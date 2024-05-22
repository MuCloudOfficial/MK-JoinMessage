plugins {
    kotlin("jvm") version "1.9.23"
}

group = "me.mucloud"
version = "SakuraOcean V1"
val dev = 1

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

