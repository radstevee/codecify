plugins {
    kotlin("jvm")
    `maven-publish`
    kotlin("kapt")
}

group = "net.radstevee.codecify.cli"

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.20")
    compileOnly("com.google.auto.service:auto-service:1.0-rc4")
    kapt("com.google.auto.service:auto-service:1.0-rc4")
    implementation(project(":codecify.k2"))
    implementation(project(":codecify.k1"))
    implementation(project(":codecify.common"))
    implementation(project(":api"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}