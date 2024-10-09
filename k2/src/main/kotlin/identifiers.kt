package net.radstevee.codecify.k2

import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

// Package Names
const val MOJANG_PACKAGE = "com.mojang"
const val SERIALIZATION_PACKAGE_NAME = "$MOJANG_PACKAGE.serialization"
val SERIALIZATION_PACKAGE = FqName(SERIALIZATION_PACKAGE_NAME)
val CODEC_PACKAGE = FqName("$SERIALIZATION_PACKAGE_NAME.codecs")
val DATAFIXERS_PACKAGE = FqName("$MOJANG_PACKAGE.datafixers")
val DATAFIXER_KIND_PACKAGE = FqName("$MOJANG_PACKAGE.datafixers.kinds")
val JAVA_FUNCTION_PACKAGE = FqName("java.util.function")
val KOTLIN_REFLECT_PACKAGE = FqName("kotlin.reflect")

// Class Names
val RECORD_CODEC_BUILDER = FqName("RecordCodecBuilder")
val MU = FqName("RecordCodecBuilder.Mu")
val APP = FqName("App")
val RECORD_CODEC_BUILDER_INSTANCE = FqName("RecordCodecBuilder.Instance")
val FUNCTION = FqName("Function")
val KFUNCTION2 = FqName("KFunction2")
fun productsN(n: Int) = FqName("Products.P$n")
val KIND1 = FqName("Kind1")
val MAP_CODEC = FqName("MapCodec")

// Function names
val CREATE = Name.identifier("create")
val APPLY = Name.identifier("apply")
val GROUP = Name.identifier("group")
val FIELD_OF = Name.identifier("fieldOf")
val FOR_GETTER = Name.identifier("forGetter")

// Variable names
val INSTANCE = Name.identifier("instance")

// Class IDs
val RECORD_CODEC_BUILDER_ID = ClassId(CODEC_PACKAGE, RECORD_CODEC_BUILDER, false)
val CODEC = FqName("Codec")
val CODEC_ID = ClassId(SERIALIZATION_PACKAGE, CODEC, false)
val MU_ID = ClassId(CODEC_PACKAGE, MU, false)
val APP_ID = ClassId(DATAFIXER_KIND_PACKAGE, APP, false)
val RECORD_CODEC_BUILDER_INSTANCE_ID = ClassId(CODEC_PACKAGE, RECORD_CODEC_BUILDER_INSTANCE, false)
fun productsNId(n: Int) = ClassId(DATAFIXERS_PACKAGE, productsN(n), false)
val FUNCTION_ID = ClassId(JAVA_FUNCTION_PACKAGE, FUNCTION, false)
val KFUNCTION2_ID = ClassId(KOTLIN_REFLECT_PACKAGE, KFUNCTION2, false)
val KIND1_ID = ClassId(DATAFIXER_KIND_PACKAGE, KIND1, false)
val MAP_CODEC_ID = ClassId(MAP_CODEC, SERIALIZATION_PACKAGE, false)