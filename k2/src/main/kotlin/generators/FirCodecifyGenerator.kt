package net.radstevee.codecify.k2.generators

import net.radstevee.codecify.k2.APPLY
import net.radstevee.codecify.k2.APP_ID
import net.radstevee.codecify.k2.CODEC_ID
import net.radstevee.codecify.k2.CREATE
import net.radstevee.codecify.k2.FIELD_OF
import net.radstevee.codecify.k2.FOR_GETTER
import net.radstevee.codecify.k2.FUNCTION_ID
import net.radstevee.codecify.k2.GROUP
import net.radstevee.codecify.k2.INSTANCE
import net.radstevee.codecify.k2.KIND1_ID
import net.radstevee.codecify.k2.MAP_CODEC_ID
import net.radstevee.codecify.k2.MU_ID
import net.radstevee.codecify.k2.RECORD_CODEC_BUILDER_ID
import net.radstevee.codecify.k2.RECORD_CODEC_BUILDER_INSTANCE_ID
import net.radstevee.codecify.k2.getField
import net.radstevee.codecify.k2.getFunctionReference
import net.radstevee.codecify.k2.getParentIfCompanion
import net.radstevee.codecify.k2.getRegularClass
import net.radstevee.codecify.k2.getType
import net.radstevee.codecify.k2.productsNId
import net.radstevee.codecify.k2.projectType
import net.radstevee.codecify.k2.resolveType
import org.jetbrains.kotlin.GeneratedDeclarationKey
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirFunctionTarget
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.primaryConstructorSymbol
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
import org.jetbrains.kotlin.fir.declarations.builder.buildAnonymousFunction
import org.jetbrains.kotlin.fir.declarations.builder.buildValueParameter
import org.jetbrains.kotlin.fir.expressions.builder.buildAnonymousFunctionExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildArgumentList
import org.jetbrains.kotlin.fir.expressions.builder.buildBlock
import org.jetbrains.kotlin.fir.expressions.builder.buildCallableReferenceAccess
import org.jetbrains.kotlin.fir.expressions.builder.buildFunctionCall
import org.jetbrains.kotlin.fir.expressions.builder.buildLiteralExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildPropertyAccessExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildReturnExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildSamConversionExpression
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.extensions.NestedClassGenerationContext
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.moduleData
import org.jetbrains.kotlin.fir.plugin.createCompanionObject
import org.jetbrains.kotlin.fir.plugin.createDefaultPrivateConstructor
import org.jetbrains.kotlin.fir.plugin.createMemberProperty
import org.jetbrains.kotlin.fir.references.builder.buildResolvedCallableReference
import org.jetbrains.kotlin.fir.references.builder.buildResolvedNamedReference
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.ConeClassLikeLookupTagImpl
import org.jetbrains.kotlin.fir.symbols.impl.FirAnonymousFunctionSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirPropertySymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirValueParameterSymbol
import org.jetbrains.kotlin.fir.types.ConeStarProjection
import org.jetbrains.kotlin.fir.types.constructType
import org.jetbrains.kotlin.fir.types.impl.ConeClassLikeTypeImpl
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.types.ConstantValueKind

class FirCodecifyGenerator(session: FirSession) : FirDeclarationGenerationExtension(session) {
    override fun generateNestedClassLikeDeclaration(
        owner: FirClassSymbol<*>, name: Name, context: NestedClassGenerationContext
    ): FirClassLikeSymbol<*>? {
        if (name != SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT) return null
        return createCompanionObject(owner, Key).symbol
    }

    @OptIn(SymbolInternals::class)
    override fun generateProperties(callableId: CallableId, context: MemberGenerationContext?): List<FirPropertySymbol> {
        val ownerCompanion = context?.owner ?: return emptyList()
        val ownerKey = (ownerCompanion.origin as? FirDeclarationOrigin.Plugin)?.key ?: return emptyList()
        if (ownerKey != Key) return emptyList()
        if (callableId.callableName != CODEC_NAME) return emptyList()
        val owner = ownerCompanion.getParentIfCompanion(session) ?: error("does not have a parent class")

        val type = ConeClassLikeTypeImpl(
            ConeClassLikeLookupTagImpl(
                CODEC_ID
            ), typeArguments = arrayOf(ConeStarProjection), false
        )
        val constructor = owner.primaryConstructorSymbol(session) ?: error("class needs a constructor")
        val codecClass = (session.getRegularClass(CODEC_ID) ?: error("DFU not in classpath"))
        val constructorParameters = constructor.valueParameterSymbols.map {
            val type = it.resolvedReturnType.type
            val typeString = type.type.toString().uppercase().removePrefix("KOTLIN/")

            Triple(
                type,
                it.name,
                codecClass.getField(Name.identifier(typeString))
                    ?: error("couldn't find ${codecClass.name}.$typeString")
            )
        }
        val ownerType = owner.constructType(arrayOf(), false)
        val prop = createMemberProperty(
            owner, ownerKey, CODEC_NAME, type
        ).apply {
            replaceInitializer(buildFunctionCall {
                typeArguments.add(projectType(ownerType))
                coneTypeOrNull = session.getType(CODEC_ID, arrayOf(ownerType))
                calleeReference = session.getFunctionReference(RECORD_CODEC_BUILDER_ID, CREATE)

                argumentList = buildArgumentList {
                    arguments.add(buildSamConversionExpression {
                        expression = buildAnonymousFunctionExpression {
                            anonymousFunction = buildAnonymousFunction {
                                returnTypeRef = session.resolveType(APP_ID, arrayOf(session.getType(MU_ID, arrayOf(ownerType))))
                                moduleData = session.moduleData
                                origin = FirDeclarationOrigin.Synthetic.PluginFile
                                symbol = FirAnonymousFunctionSymbol()
                                isLambda = true
                                hasExplicitParameterList = true

                                valueParameters.add(buildValueParameter {
                                    moduleData = session.moduleData
                                    origin = FirDeclarationOrigin.Synthetic.PluginFile
                                    name = INSTANCE
                                    symbol = FirValueParameterSymbol(name)
                                    isCrossinline = false
                                    isNoinline = false
                                    isVararg = false
                                    containingFunctionSymbol = FirAnonymousFunctionSymbol()
                                    returnTypeRef = session.resolveType(RECORD_CODEC_BUILDER_INSTANCE_ID, arrayOf(ownerType))
                                })

                                body = buildBlock {
                                    statements.add(buildReturnExpression {
                                        target = FirFunctionTarget(null, true)
                                        result = buildFunctionCall {
                                            coneTypeOrNull = session.getType(APP_ID, arrayOf(session.getType(MU_ID, arrayOf(ownerType)), ownerType))
                                            typeArguments.add(projectType(ownerType))
                                            calleeReference = session.getFunctionReference(productsNId(constructorParameters.size), APPLY)
                                            argumentList = buildArgumentList {
                                                arguments.add(buildPropertyAccessExpression {
                                                    calleeReference = buildResolvedNamedReference {
                                                        resolvedSymbol = FirValueParameterSymbol(INSTANCE)
                                                        name = INSTANCE
                                                    }
                                                })

                                                arguments.add(buildSamConversionExpression {
                                                    expression = buildCallableReferenceAccess {
                                                        calleeReference = buildResolvedCallableReference {
                                                            name = owner.name
                                                            resolvedSymbol = owner
                                                            mappedArguments = mapOf()
                                                        }
                                                    }
                                                })
                                            }

                                            dispatchReceiver = buildFunctionCall {
                                                calleeReference = session.getFunctionReference(KIND1_ID, GROUP, constructorParameters.size - 1)
                                                constructorParameters.forEach { (param) ->
                                                    typeArguments.add(projectType(param))
                                                }
                                                argumentList = buildArgumentList {
                                                    constructorParameters.forEach { (type, paramName, codecField) ->
                                                        arguments.add(buildFunctionCall {
                                                            dispatchReceiver = buildFunctionCall {
                                                                calleeReference = session.getFunctionReference(CODEC_ID, FIELD_OF)
                                                                dispatchReceiver = buildPropertyAccessExpression {
                                                                    calleeReference = buildResolvedNamedReference {
                                                                        this.name = codecField.name
                                                                        resolvedSymbol = codecField
                                                                    }
                                                                }

                                                                argumentList = buildArgumentList {
                                                                    arguments.add(
                                                                        buildLiteralExpression(
                                                                            null, ConstantValueKind.String, paramName, null, true
                                                                        )
                                                                    )
                                                                }
                                                            }

                                                            calleeReference = session.getFunctionReference(MAP_CODEC_ID, FOR_GETTER)
                                                            argumentList = buildArgumentList {
                                                                arguments.add(buildSamConversionExpression {
                                                                    coneTypeOrNull = session.getType(FUNCTION_ID, arrayOf(ownerType, type))

                                                                    expression = buildCallableReferenceAccess {
                                                                        calleeReference = buildResolvedCallableReference {

                                                                        }
                                                                    }
                                                                })
                                                            }
                                                        })
                                                    }
                                                }
                                            }
                                        }
                                    })
                                }
                            }
                        }
                    })
                }
            })
        }
        error("HOLY FUCKING SHIT")
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
        override fun toString() = "CodecifyGeneratorKey"
    }

    private companion object {
        val PREDICATE = LookupPredicate.create { annotatedOrUnder(ClassId.fromString("net.radstevee.codecify.Codecify").asSingleFqName()) }
        val CODEC_NAME = Name.identifier("CODEC")
    }
}