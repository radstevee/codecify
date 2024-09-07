package net.radstevee.codecify

import kotlin.reflect.KClass

annotation class Codec<T : Any>(val forClass: KClass<T>)
annotation class Codecify