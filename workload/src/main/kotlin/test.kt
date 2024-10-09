package net.radstevee.codecify

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

class MyClass(val a: String, val b: Int, val c: Boolean, val d: String) {
    companion object {
        val CODEC = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.fieldOf("a").forGetter(MyClass::a),
                Codec.INT.fieldOf("b").forGetter(MyClass::b),
                Codec.BOOL.fieldOf("c").forGetter(MyClass::c),
                Codec.STRING.fieldOf("d").forGetter(MyClass::d)
            ).apply(instance, ::MyClass)
        }
    }
}

fun main() {
    println(MyClass.CODEC)
}