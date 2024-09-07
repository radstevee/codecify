package net.radstevee.codecify.k1.codegen

import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.com.intellij.util.io.StringRef
import org.jetbrains.kotlin.constant.StringValue
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.stubs.impl.KotlinPropertyStubImpl
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

object RecordCodecGenerator {
    fun create(classDescriptor: ClassDescriptor): KtProperty {
        return KtProperty(
            KotlinPropertyStubImpl(
                null,
                StringRef.fromString("CODEC"),
                false,
                false,
                false,
                false,
                true,
                false,
                true,
                classDescriptor.fqNameSafe.child(Name.identifier("CODEC")),
                StringValue("HELLO FROM K1"),
                null
            )
        )
    }
}