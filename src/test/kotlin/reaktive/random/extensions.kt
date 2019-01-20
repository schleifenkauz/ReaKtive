package reaktive.random

fun <T, F> Gen<T>.map(f: (T) -> F) = Gen { f(next()) }
fun <T, F> Gen<T>.flatMap(f: (T) -> Gen<F>) =
    Gen { f(next()).next() }