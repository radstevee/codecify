plugins {
    kotlin("jvm")
    id("net.radstevee.codecify.codecify") version "0.1.0"
}
group = "net.radstevee.codecify.workload"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":gradle-plugin"))
    implementation(project(":api"))

    implementation("com.mojang:datafixerupper:7.0.14")
}
