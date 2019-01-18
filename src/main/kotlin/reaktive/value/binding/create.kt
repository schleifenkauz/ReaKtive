/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.Dependencies
import reaktive.value.ReactiveValue
import reaktive.value.binding.impl.BindingImpl
import reaktive.value.reactiveValue

/**
 * Return a [Binding] initially set to [initialValue] and executes [body]
 */
fun <T> binding(initialValue: T, body: ValueBindingBody<T>.() -> Unit): Binding<T> =
    BindingImpl(initialValue, body)

/**
 * Return a [Binding] which is recomputed if one of the [dependencies] is invalidated
 * @param compute the function the is used to compute the value of the returned [Binding]
 */
inline fun <T> binding(dependencies: Dependencies, crossinline compute: () -> T): Binding<T> =
    binding(compute()) {
        val obs = dependencies.observe {
                set(compute())
            }
            addObserver(obs)
        }

/**
 * @return a [Binding] wrapping the receiver
 * * Disposing the returned binding has no effect
 */
fun <T> ReactiveValue<T>.asBinding(): Binding<T> {
    return object : ReactiveValue<T> by this, Binding<T> {
        override fun dispose() {}
    }
}

/**
 * @return a constant binding
 * * Disposing the returned binding has no effect
 */
fun <T> binding(value: T): Binding<T> {
    return reactiveValue(value).asBinding()
}
