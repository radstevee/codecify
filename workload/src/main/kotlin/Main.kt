package net.radstevee.codecify

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

@Codecify
data class MyClass(val string: String, val int: Int)

@Codecify
class MyEmptyClass(val myClass: MyClass)

val EXAMPLE_CODEC = RecordCodecBuilder.create { instance ->
    instance.group(
        Codec.STRING.fieldOf("string").forGetter(MyClass::string),
        Codec.INT.fieldOf("int").forGetter(MyClass::int)
    ).apply(instance, ::MyClass)
}

fun main() {
    println("Hello Wsorld!")
}