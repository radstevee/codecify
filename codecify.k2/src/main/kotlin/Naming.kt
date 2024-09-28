package net.radstevee.codecify.k2

enum class NamingCase {
    CAMEL_CASE,
    PASCAL_CASE,
    SNAKE_CASE
}

fun String.rename(case: NamingCase) = this // TODO: implement