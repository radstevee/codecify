package net.radstevee.codecify.k2.generators

import org.jetbrains.kotlin.GeneratedDeclarationKey
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirImplementationDetail
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
import org.jetbrains.kotlin.fir.declarations.builder.buildProperty
import org.jetbrains.kotlin.fir.declarations.impl.FirResolvedDeclarationStatusImpl
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.extensions.NestedClassGenerationContext
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.moduleData
import org.jetbrains.kotlin.fir.plugin.createCompanionObject
import org.jetbrains.kotlin.fir.plugin.createDefaultPrivateConstructor
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirPropertySymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.fir.types.builder.buildDynamicTypeRef
import org.jetbrains.kotlin.fir.types.builder.buildFunctionTypeRef
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

class CodecGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {
    override fun generateNestedClassLikeDeclaration(
        owner: FirClassSymbol<*>,
        name: Name,
        context: NestedClassGenerationContext
    ): FirClassLikeSymbol<*>? {
        if (name != SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT) return null
        return createCompanionObject(owner, Key).symbol
    }

    @OptIn(FirImplementationDetail::class)
    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        val owner = context?.owner ?: return emptyList()
        val ownerKey = (owner.origin as? FirDeclarationOrigin.Plugin)?.key ?: return emptyList()
        if (ownerKey != Key) return emptyList()
        if (callableId.callableName != CODEC_NAME) return emptyList()
        val prop = buildProperty {
            moduleData = session.moduleData
            origin = FirDeclarationOrigin.Java.Library
            status = FirResolvedDeclarationStatusImpl.DEFAULT_STATUS_FOR_STATUSLESS_DECLARATIONS
            symbol = FirPropertySymbol(CallableId(Name.identifier(owner.name.toString() + ".$CODEC_NAME")))
            name = CODEC_NAME
            returnTypeRef = buildFunctionTypeRef {
                returnTypeRef = buildDynamicTypeRef {
                    isMarkedNullable = false
                }

                isMarkedNullable = false
                isSuspend = false
            }
            isVar = false
            isLocal = true
        }
        return listOf(prop.symbol)
    }

    override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
        val constructor = createDefaultPrivateConstructor(context.owner, Key)
        return listOf(constructor.symbol)
    }

    override fun getCallableNamesForClass(classSymbol: FirClassSymbol<*>, context: MemberGenerationContext): Set<Name> {
        if (classSymbol.classKind != ClassKind.OBJECT) return emptySet()
        if (classSymbol !is FirRegularClassSymbol) return emptySet()
        val classId = classSymbol.classId
        if (!classId.isNestedClass || classId.shortClassName != SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT) return emptySet()
        val origin = classSymbol.origin as? FirDeclarationOrigin.Plugin
        return if (origin?.key == Key) {
            setOf(CODEC_NAME, SpecialNames.INIT)
        } else {
            setOf(CODEC_NAME)
        }
    }

    override fun getNestedClassifiersNames(classSymbol: FirClassSymbol<*>, context: NestedClassGenerationContext): Set<Name> {
        return if (session.predicateBasedProvider.matches(PREDICATE, classSymbol)) {
            setOf(SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT)
        } else {
            emptySet()
        }
    }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(PREDICATE)
    }

    object Key : GeneratedDeclarationKey() {
        override fun toString() = "CodecGeneratorKey"
    }

    private companion object {
        val PREDICATE = LookupPredicate.create { annotated(ClassId.fromString("net.radstevee.codecify.Codecify").asSingleFqName()) }
        val CODEC_NAME = Name.identifier("CODEC")
    }
}