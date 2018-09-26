/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value.binding

import org.nikok.reaktive.Dependencies
import org.nikok.reaktive.observe
import org.nikok.reaktive.value.ReactiveValue
import org.nikok.reaktive.value.binding.impl.BindingImpl
import org.nikok.reaktive.value.reactiveValue

/**
 * Return a [Binding] with [description] and initially set to [initialValue] and executes [body]
 */
fun <T> binding(description: String, initialValue: T, body: ValueBindingBody<T>.() -> Unit): Binding<T> =
        BindingImpl(description, initialValue, body)

/**
 * Return a [Binding] described by [description] which is recomputed if one of the [dependencies] is invalidated
 * @param compute the function the is used to compute the value of the returned [Binding]
 */
fun <T> binding(description: String, dependencies: Dependencies, compute: () -> T): Binding<T> =
        binding(description, compute()) {
            val obs = dependencies.observe("update $description") { _ ->
                set(compute())
            }
            addObserver(obs)
        }

/**
 * @return a [Binding] wrapping the receiver
 * * Disposing the returned binding has no effect
 */
fun <T> ReactiveValue<T>.asBinding(description: String = this.description): Binding<T> {
    return object : ReactiveValue<T> by this, Binding<T> {
        override fun dispose() {}
        override val description: String = description
    }
}

/**
 * @return a constant binding
 * * Disposing the returned binding has no effect
 */
fun <T> binding(description: String, value: T): Binding<T> {
    return reactiveValue(description, value).asBinding()
}
