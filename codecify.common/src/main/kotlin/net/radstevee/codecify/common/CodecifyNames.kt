package net.radstevee.codecify.common

import org.jetbrains.kotlin.name.ClassId

object CodecifyNames {
    val CODEC = ClassId.fromString("net.radstevee.codecify.Codec")
    val CODECIFY = ClassId.fromString("net.radstevee.codecify.Codecify")
    const val COMPILER_PLUGIN_ID = "codecify"
}