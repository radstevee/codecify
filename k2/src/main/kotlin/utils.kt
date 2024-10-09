package net.radstevee.codecify.k2

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.references.builder.buildResolvedNamedReference
import org.jetbrains.kotlin.fir.resolve.providers.getClassDeclaredFunctionSymbols
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirFieldSymbol
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.fir.types.ConeTypeProjection
import org.jetbrains.kotlin.fir.types.builder.buildResolvedTypeRef
import org.jetbrains.kotlin.fir.types.builder.buildTypeProjectionWithVariance
import org.jetbrains.kotlin.fir.types.constructType
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.types.Variance

fun projectType(type: ConeKotlinType, variance: Variance = Variance.INVARIANT) = buildTypeProjectionWithVariance {
    this.variance = variance
    typeRef = buildResolvedTypeRef { this.type = type }
}

fun FirSession.getType(classId: ClassId, typeArguments: Array<ConeTypeProjection> = arrayOf(), isNullable: Boolean = false) =
    symbolProvider.getClassLikeSymbolByClassId(classId)?.constructType(typeArguments, isNullable) ?: error("type $classId not found")
fun FirSession.resolveType(classId: ClassId, typeArguments: Array<ConeTypeProjection> = arrayOf(), isNullable: Boolean = false) =
    buildResolvedTypeRef { type = getType(classId, typeArguments, isNullable) }
fun FirSession.typeProjection(
    classId: ClassId, typeArguments: Array<ConeTypeProjection> = arrayOf(), isNullable: Boolean = false, variance: Variance = Variance.INVARIANT
) = projectType(getType(classId, typeArguments, isNullable), variance)
fun FirSession.resolveFunction(owner: ClassId, name: Name) = symbolProvider.getClassDeclaredFunctionSymbols(owner, name)
fun FirSession.resolveFirstFunction(owner: ClassId, name: Name) = resolveFunction(owner, name).first()
fun FirSession.getFunctionReference(owner: ClassId, name: Name, index: Int = 0) = buildResolvedNamedReference {
    this.name = name
    resolvedSymbol = resolveFunction(owner, name)[index]
}
fun FirSession.getClass(classId: ClassId) = symbolProvider.getClassLikeSymbolByClassId(classId)
fun FirSession.getRegularClass(classId: ClassId) = getClass(classId) as? FirClassSymbol<*>

fun ClassId.removeSuffix(suffix: String) = ClassId(packageFqName, FqName(relativeClassName.asString().removeSuffix(suffix)), isLocal)

fun FirClassSymbol<*>.getField(name: Name) = declarationSymbols.filterIsInstance<FirFieldSymbol>().find { it.name == name }
fun FirClassSymbol<*>.getParentIfCompanion(session: FirSession) = if (classId.relativeClassName.asString().endsWith(".Companion")) {
    session.getRegularClass(classId.removeSuffix(".Companion"))
} else {
    null
}