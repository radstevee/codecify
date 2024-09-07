package net.radstevee.codecify.k1.processor

import net.radstevee.net.radstevee.codecify.k1.processor.SyntheticPartsBuilder


import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.load.java.lazy.LazyJavaResolverContext

interface Processor {
    fun contribute(classDescriptor: ClassDescriptor, partsBuilder: SyntheticPartsBuilder, c: LazyJavaResolverContext)
}