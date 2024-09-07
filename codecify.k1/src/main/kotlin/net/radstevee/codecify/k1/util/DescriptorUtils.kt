package net.radstevee.codecify.k1.util

import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.builtins.jvm.CloneableClassScope
import org.jetbrains.kotlin.descriptors.CallableMemberDescriptor
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.FieldDescriptor
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.SourceElement
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.ClassDescriptorImpl
import org.jetbrains.kotlin.descriptors.impl.FieldDescriptorImpl
import org.jetbrains.kotlin.descriptors.impl.PropertyDescriptorImpl
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.source.KotlinSourceElement
import org.jetbrains.kotlin.storage.LockBasedStorageManager
import org.jetbrains.kotlin.types.KotlinType

fun ClassDescriptor.createCompanionObject(): ClassDescriptor {
    val descriptor = ClassDescriptorImpl(
        this,
        Name.identifier("Companion"),
        Modality.FINAL,
        ClassKind.OBJECT,
        listOf<KotlinType>(),
        this as SourceElement,
        false,
        LockBasedStorageManager.NO_LOCKS
    )

    descriptor.initialize(CloneableClassScope(LockBasedStorageManager.NO_LOCKS, this), setOf(), null)

    return descriptor
}

fun ClassDescriptor.createField(name: Name, source: KotlinSourceElement): FieldDescriptor {
    val property = PropertyDescriptorImpl.create(
        this,
        Annotations.create(listOf()),
        Modality.FINAL,
        DescriptorVisibilities.PUBLIC,
        false,
        name,
        CallableMemberDescriptor.Kind.DECLARATION,
        source,
        false,
        false,
        false,
        false,
        false,
        false
    )
    val descriptor = FieldDescriptorImpl(Annotations.create(listOf()), property)

    return descriptor
}