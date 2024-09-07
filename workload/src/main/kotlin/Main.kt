package net.radstevee.codecify

@Codecify
data class MyClass(val string: String, val int: Int) {
    companion object {
        const val A = "a!!!!"
    }
}

@Codecify
class MyEmptyClass(val myClass: MyClass) {
    fun hi() {

    }
}

fun main() {
    println("Hello World!")
}