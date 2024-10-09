pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
    }
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "codecify"
include("workload")
include("cli")
include("api")
include("k2")
include("common")
include("gradle-plugin")
