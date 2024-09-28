package net.radstevee.codecify.k2

import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

val CODEC_PACKAGE = FqName("com.mojang.serialization.codecs")
val RECORD_CODEC_BUILDER = FqName("RecordCodecBuilder")
val RECORD_CODEC_BUILDER_CREATE = Name.identifier("create")
val RECORD_CODEC_BUILDER_CREATE_FULL = Name.identifier("$CODEC_PACKAGE.$RECORD_CODEC_BUILDER.create")
val RECORD_CODEC_BUILDER_ID = ClassId(CODEC_PACKAGE, RECORD_CODEC_BUILDER, false)

val RECORD_CODEC_BUILDER_CALLABLE = CallableId(CODEC_PACKAGE, RECORD_CODEC_BUILDER, RECORD_CODEC_BUILDER_CREATE)