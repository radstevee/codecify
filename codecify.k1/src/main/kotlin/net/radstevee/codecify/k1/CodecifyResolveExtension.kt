package net.radstevee.codecify.k1

import org.jetbrains.kotlin.resolve.jvm.extensions.SyntheticJavaResolveExtension

class CodecifyResolveExtension : SyntheticJavaResolveExtension {
    override fun buildProvider() = CodecifySyntheticJavaPartsProvider()
}