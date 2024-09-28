package net.radstevee.codecify.k2

import org.jetbrains.kotlin.KtSourceElement
import org.jetbrains.kotlin.fir.references.FirResolvedNamedReference
import org.jetbrains.kotlin.fir.symbols.FirBasedSymbol
import org.jetbrains.kotlin.fir.visitors.FirTransformer
import org.jetbrains.kotlin.fir.visitors.FirVisitor
import org.jetbrains.kotlin.name.Name

class FirResolvedNamedReferenceImpl(
    override val source: KtSourceElement?,
    override val name: Name,
    override val resolvedSymbol: FirBasedSymbol<*>,
) : FirResolvedNamedReference() {

    override fun <R, D> acceptChildren(visitor: FirVisitor<R, D>, data: D) {}

    override fun <D> transformChildren(transformer: FirTransformer<D>, data: D): net.radstevee.codecify.k2.FirResolvedNamedReferenceImpl {
        return this
    }
}