package net.radstevee.codecify.k1.processor

import net.radstevee.codecify.k1.codegen.RecordCodecGenerator
import net.radstevee.codecify.k1.util.CodecifyAnnotations
import net.radstevee.codecify.k1.util.createCompanionObject
import net.radstevee.codecify.k1.util.createField
import net.radstevee.net.radstevee.codecify.k1.processor.SyntheticPartsBuilder
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.load.java.lazy.LazyJavaResolverContext
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.source.KotlinSourceElement

object CodecifyProcessor : Processor {
    override fun contribute(
        classDescriptor: ClassDescriptor,
        partsBuilder: SyntheticPartsBuilder,
        c: LazyJavaResolverContext
    ) {
        error("HELLO")
        val clCodecify = CodecifyAnnotations.Codecify.getOrNull(classDescriptor) ?: return

        val companionObject = classDescriptor.companionObjectDescriptor ?: classDescriptor.createCompanionObject()

        companionObject.createField(Name.identifier("CODEC"), KotlinSourceElement(RecordCodecGenerator.create(classDescriptor)))
    }
}