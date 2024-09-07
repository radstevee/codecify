package net.radstevee.codecify.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class CodecifyGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create(
            "codecify",
            CodecifyGradleExtension::class.java
        )
    }
}

open class CodecifyGradleExtension {
    var enabled = true
}