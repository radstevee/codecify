package net.radstevee.codecify.k2.generators

import net.radstevee.codecify.common.CodecifyNames
import org.jetbrains.kotlin.fir.FirAnnotationContainer
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.getStringArgument
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.fir.types.coneType
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

fun List<FirAnnotation>.findAnnotation(classId: ClassId): FirAnnotation? {
    return firstOrNull { it.annotationTypeRef.coneType.classId == classId }
}

abstract class ConeAnnotationCompanion<T>(val name: ClassId) {
    abstract fun extract(annotation: FirAnnotation, session: FirSession): T

    fun getOrNull(annotated: FirAnnotationContainer, session: FirSession): T? {
        return annotated.annotations.findAnnotation(name)?.let { this.extract(it, session) }
    }
}

abstract class ConeAnnotationAndConfigCompanion<T>(val annotationName: ClassId) {
    abstract fun extract(annotation: FirAnnotation?, session: FirSession): T

    /**
     * Get from annotation or config or default
     */
    fun get(annotated: FirAnnotationContainer, session: FirSession): T =
        extract(annotated.annotations.findAnnotation(annotationName), session)

    /**
     * If element is annotated, get from it or config or default
     */
    fun getIfAnnotated(annotated: FirAnnotationContainer, session: FirSession): T? =
        annotated.annotations.findAnnotation(annotationName)?.let { annotation ->
            extract(annotation, session)
        }
}

object ConeCodecifyExtensions {
    class Codecify(
        val rename: String = "camelCase"
    ) {
        companion object : ConeAnnotationCompanion<Codecify>(CodecifyNames.CODECIFY) {
            override fun extract(annotation: FirAnnotation, session: FirSession): Codecify {
                return Codecify(
                    rename = annotation.getStringArgument(Name.identifier("rename"), session) ?: "camelCase"
                )
            }
        }
    }
}