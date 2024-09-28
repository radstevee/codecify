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
include("codecify.cli")
include("api")
include("codecify.k2")
include("codecify.common")
include("gradle-plugin")
