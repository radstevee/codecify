package net.radstevee.codecify.k1

import net.radstevee.codecify.k1.processor.CodecifyProcessor
import net.radstevee.codecify.k1.processor.Processor
import net.radstevee.codecify.k1.processor.ValueFieldModifier
import net.radstevee.net.radstevee.codecify.k1.processor.SyntheticParts
import net.radstevee.net.radstevee.codecify.k1.processor.SyntheticPartsBuilder
import org.jetbrains.kotlin.descriptors.ClassConstructorDescriptor
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.descriptors.impl.PropertyDescriptorImpl
import org.jetbrains.kotlin.load.java.lazy.LazyJavaResolverContext
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.jvm.SyntheticJavaPartsProvider

class CodecifySyntheticJavaPartsProvider : SyntheticJavaPartsProvider {
    override fun getMethodNames(
        thisDescriptor: ClassDescriptor,
        c: LazyJavaResolverContext
    ) = c.getSyntheticParts(thisDescriptor).methods.map { it.name }

    override fun generateMethods(
        thisDescriptor: ClassDescriptor,
        name: Name,
        result: MutableCollection<SimpleFunctionDescriptor>,
        c: LazyJavaResolverContext
    ) {
        val methods = c.getSyntheticParts(thisDescriptor).methods.filter { it.name == name }
        addNonExistent(result, methods)
    }

    override fun getStaticFunctionNames(thisDescriptor: ClassDescriptor, c: LazyJavaResolverContext): List<Name> =
        c.getSyntheticParts(thisDescriptor).staticFunctions.map { it.name }

    override fun generateStaticFunctions(
        thisDescriptor: ClassDescriptor,
        name: Name,
        result: MutableCollection<SimpleFunctionDescriptor>,
        c: LazyJavaResolverContext,
    ) {
        val functions = c.getSyntheticParts(thisDescriptor).staticFunctions.filter { it.name == name }
        addNonExistent(result, functions)
    }

    override fun generateConstructors(
        thisDescriptor: ClassDescriptor,
        result: MutableList<ClassConstructorDescriptor>,
        c: LazyJavaResolverContext,
    ) {
        val constructors = c.getSyntheticParts(thisDescriptor).constructors
        addNonExistent(result, constructors)
    }

    override fun getNestedClassNames(thisDescriptor: ClassDescriptor, c: LazyJavaResolverContext): List<Name> {
        return c.getSyntheticParts(thisDescriptor).classes.map { it.name }
    }

    override fun generateNestedClass(
        thisDescriptor: ClassDescriptor,
        name: Name,
        result: MutableList<ClassDescriptor>,
        c: LazyJavaResolverContext,
    ) {
        result += c.getSyntheticParts(thisDescriptor).classes.filter { it.name == name }
    }

    override fun modifyField(
        thisDescriptor: ClassDescriptor,
        propertyDescriptor: PropertyDescriptorImpl,
        c: LazyJavaResolverContext
    ): PropertyDescriptorImpl {
        return ValueFieldModifier.modifyField(propertyDescriptor) ?: propertyDescriptor
    }

    private fun <T : FunctionDescriptor> addNonExistent(result: MutableCollection<T>, toAdd: List<T>) {
        toAdd.forEach { f ->
            if (result.none { sameSignature(it, f) }) {
                result += f
            }
        }
    }

    private fun LazyJavaResolverContext.getSyntheticParts(descriptor: ClassDescriptor): SyntheticParts {
        // (descriptor !is LazyJavaClassDescriptor && descriptor !is SyntheticJavaClassDescriptor) return SyntheticParts.Companion.Empty
        return partsCache.getOrPut(descriptor) {
            computeSyntheticParts(descriptor)
        }
    }

    private fun LazyJavaResolverContext.computeSyntheticParts(descriptor: ClassDescriptor): SyntheticParts {
        val builder = SyntheticPartsBuilder()
        error("HELLO WORLD FROM COMPUTE PARTS")
        processors.forEach { it.contribute(descriptor, builder, this) }
        return builder.build()
    }

    private val partsCache: MutableMap<ClassDescriptor, SyntheticParts> = HashMap()
    private val processors = listOf<Processor>(CodecifyProcessor)

    companion object {
        private fun sameSignature(a: FunctionDescriptor, b: FunctionDescriptor): Boolean {
            val aVararg = a.valueParameters.any { it.varargElementType != null }
            val bVararg = b.valueParameters.any { it.varargElementType != null }
            return aVararg && bVararg ||
                    aVararg && b.valueParameters.size >= (a.valueParameters.size - 1) ||
                    bVararg && a.valueParameters.size >= (b.valueParameters.size - 1) ||
                    a.valueParameters.size == b.valueParameters.size
        }
    }
}