plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "net.radstevee.codecify.k2"

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.20")
    implementation(project(":codecify.common"))
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