plugins {
    kotlin("jvm")
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://libraries.minecraft.net/")
    }

    version = "0.1.0"
    apply(plugin = "maven-publish")
}