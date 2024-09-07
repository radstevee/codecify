plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "net.radstevee.codecify.api"

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