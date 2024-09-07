@file:OptIn(ExperimentalCompilerApi::class)

package net.radstevee.codecify.cli

import com.google.auto.service.AutoService
import net.radstevee.codecify.common.CodecifyNames
import net.radstevee.codecify.k1.CodecifyResolveExtension
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.resolve.jvm.extensions.SyntheticJavaResolveExtension

@AutoService(CommandLineProcessor::class)
class CodecifyComponentRegistrar : CompilerPluginRegistrar() {
    companion object {
        fun registerComponents(extensionStorage: ExtensionStorage, compilerConfiguration: CompilerConfiguration) =
            with(extensionStorage) {
                SyntheticJavaResolveExtension.registerExtension(CodecifyResolveExtension())
            }
    }

    override val supportsK2 = false

    override fun ExtensionStorage.registerExtensions(
        configuration: CompilerConfiguration
    ) {
        error("HELLO FROM REGISTER EXTENSIONS")
        registerComponents(this, configuration)
    }
}

@AutoService(CommandLineProcessor::class)
class CodecifyCommandLineProcessor : CommandLineProcessor {
    init {
        error("HELLO FROM CLI PROCESSOR")
    }

    override val pluginId = CodecifyNames.COMPILER_PLUGIN_ID
    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption("enabled", "<true|false>", "whether plugin is enabled")
    )
}