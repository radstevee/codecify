package net.radstevee.codecify.k1.util

import net.radstevee.codecify.common.CodecifyNames
import org.jetbrains.kotlin.descriptors.annotations.Annotated
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.annotations.getArgumentValueOrNull
import kotlin.reflect.KClass

abstract class AnnotationCompanion<T>(val name: FqName) {
    abstract fun extract(annotation: AnnotationDescriptor): T

    fun getOrNull(annotated: Annotated): T? =
        annotated.annotations.findAnnotation(name)?.let(this::extract)
}

abstract class AnnotationAndConfigCompanion<T>(private val annotationName: FqName) {
    abstract fun extract(annotation: AnnotationDescriptor?): T

    fun get(annotated: Annotated): T =
        extract(annotated.annotations.findAnnotation(annotationName))

    fun getIfAnnotated(annotated: Annotated): T? =
        annotated.annotations.findAnnotation(annotationName)?.let { annotation ->
            extract(annotation)
        }

    fun getOrNull(annotated: Annotated): T? =
        annotated.annotations.findAnnotation(annotationName)?.let(this::extract)

}

object CodecifyAnnotations {
    class Codec<T : Any>(val forClass: KClass<T>) {
        companion object : AnnotationAndConfigCompanion<Codec<*>>(CodecifyNames.CODEC) {
            override fun extract(annotation: AnnotationDescriptor?): Codec<*> {
                val klass = annotation?.getArgumentValueOrNull<KClass<*>>("forClass") ?: error("annotation has no forClass parameter")
                return Codec(klass)
            }
        }
    }

    class Codecify {
        companion object : AnnotationCompanion<Codecify>(CodecifyNames.CODECIFY) {
            override fun extract(annotation: AnnotationDescriptor) = Codecify()
        }
    }
}