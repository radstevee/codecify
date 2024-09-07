plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "net.radstevee.codecify.gradle-plugin"

dependencies {
    compileOnly("com.google.auto.service:auto-service:1.0-rc4")
    kapt("com.google.auto.service:auto-service:1.0-rc4")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.0.20")
    implementation(project(":codecify.common"))
}

gradlePlugin {
    plugins {
        create("codecify") {
            id = "net.radstevee.codecify.codecify"
            implementationClass = "net.radstevee.codecify.gradle.CodecifyGradlePlugin"
            displayName = "Codecify"
            description = "Kotlin compiler plugin for DFU Codec Generation"
            tags = listOf("minecraft", "dfu", "mojang", "codec", "kotlin")
        }
    }

    website = "https://github.com/radstevee/codecify"
    vcsUrl = website
}

publishing {
    repositories {
        mavenLocal()
    }
}