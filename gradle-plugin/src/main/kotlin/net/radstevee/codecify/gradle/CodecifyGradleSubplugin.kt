package net.radstevee.codecify.gradle

import net.radstevee.codecify.common.CodecifyNames
import org.gradle.api.internal.provider.DefaultProviderFactory
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import java.util.concurrent.Callable

class CodecifyGradleSubplugin : KotlinCompilerPluginSupportPlugin {
    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val extension = kotlinCompilation.project.extensions.findByType(CodecifyGradleExtension::class.java)
            ?: CodecifyGradleExtension()
        if (!extension.enabled) kotlinCompilation.project.logger.info("Codecify disabled.")

        return DefaultProviderFactory().provider(Callable {
            return@Callable listOf(SubpluginOption("enabled", extension.enabled.toString()))
        })
    }

    override fun getCompilerPluginId() = CodecifyNames.COMPILER_PLUGIN_ID

    override fun getPluginArtifact() = SubpluginArtifact(
        groupId = "net.radstevee.codecify.cli", artifactId = "codecify.cli", version = "0.1.0"
    )

    override fun isApplicable(
        kotlinCompilation: KotlinCompilation<*>
    ) = kotlinCompilation.project.plugins.any { it is CodecifyGradlePlugin }
}