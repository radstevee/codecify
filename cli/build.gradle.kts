plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "net.radstevee.codecify.cli"

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.20")
    implementation(project(":k2"))
    implementation(project(":common"))
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
