package net.radstevee.codecify.k1.processor

import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.impl.PropertyDescriptorImpl
import org.jetbrains.kotlin.load.java.JavaDescriptorVisibilities

object ValueFieldModifier {
    fun modifyField(propertyDescriptor: PropertyDescriptorImpl): PropertyDescriptorImpl? {
        if (propertyDescriptor.visibility != JavaDescriptorVisibilities.PACKAGE_VISIBILITY) return null
        return propertyDescriptor.newCopyBuilder().apply {
            setVisibility(DescriptorVisibilities.PRIVATE)
        }.build() as? PropertyDescriptorImpl
    }
}
