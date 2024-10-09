@file:OptIn(ExperimentalCompilerApi::class)

package net.radstevee.codecify.cli

import net.radstevee.codecify.common.CodecifyNames
import net.radstevee.codecify.k2.FirCodecifyRegistrar
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

class CodecifyComponentRegistrar : CompilerPluginRegistrar() {
    override val supportsK2 = true

    override fun ExtensionStorage.registerExtensions(
        configuration: CompilerConfiguration
    ) {
        FirExtensionRegistrarAdapter.registerExtension(FirCodecifyRegistrar())
    }
}

class CodecifyCommandLineProcessor : CommandLineProcessor {
    override val pluginId = CodecifyNames.COMPILER_PLUGIN_ID
    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption("enabled", "<true|false>", "whether plugin is enabled")
    )
}
