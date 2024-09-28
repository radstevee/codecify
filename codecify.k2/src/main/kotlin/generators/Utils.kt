package net.radstevee.codecify.k2.generators

internal inline fun <A, B, C> uncurry(crossinline f: (A, B) -> C): (Pair<A, B>) -> C = { (a, b) -> f(a, b) }