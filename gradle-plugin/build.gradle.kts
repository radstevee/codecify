plugins {
    kotlin("jvm")
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "net.radstevee.codecify.gradle-plugin"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.0.20")
    implementation(project(":common"))
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
