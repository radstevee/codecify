package net.radstevee.codecify.k2

import net.radstevee.codecify.k2.generators.FirCodecifyGenerator
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class FirCodecifyRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::FirCodecifyGenerator
    }
}