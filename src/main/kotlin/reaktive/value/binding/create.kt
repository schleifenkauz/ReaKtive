/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.*
import reaktive.value.*
import reaktive.value.binding.impl.BindingImpl

/**
 * Return a [Binding] initially set to [initialValue] and executes [body]
 */
inline fun <T> binding(initialValue: T, body: ValueBindingBody<T>.() -> Unit): Binding<T> =
    BindingImpl.newBinding(initialValue, body)

/**
 * Return a [Binding] which is recomputed if one of the [dependencies] is invalidated
 * @param compute the function the is used to compute the value of the returned [Binding]
 */
inline fun <T> binding(dependencies: Reactive, crossinline compute: () -> T): Binding<T> =
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
    if (this is Binding<T>) return this
    return object : ReactiveValue<T> by this, Binding<T> {
        override fun dispose() {}
    }
}

/**
 * @return a constant binding
 * * Disposing the returned binding has no effect
 */
fun <T> constantBinding(value: T): Binding<T> {
    return reactiveValue(value).asBinding()
}

fun <A, B, C> binding(v1: ReactiveValue<A>, v2: ReactiveValue<B>, f: (A, B) -> C) =
    binding<C>(dependencies(v1, v2)) { f(v1.now, v2.now) }

fun <A, B, C, D> binding(v1: ReactiveValue<A>, v2: ReactiveValue<B>, v3: ReactiveValue<C>, f: (A, B, C) -> D) =
    binding<D>(dependencies(v1, v2)) { f(v1.now, v2.now, v3.now) }

fun <A, B, C, D, E> binding(
    v1: ReactiveValue<A>,
    v2: ReactiveValue<B>,
    v3: ReactiveValue<C>,
    v4: ReactiveValue<D>,
    f: (A, B, C, D) -> E
) = binding<E>(dependencies(v1, v2)) { f(v1.now, v2.now, v3.now, v4.now) }

fun <A, B, C, D, E, F> binding(
    v1: ReactiveValue<A>,
    v2: ReactiveValue<B>,
    v3: ReactiveValue<C>,
    v4: ReactiveValue<D>,
    v5: ReactiveValue<E>,
    f: (A, B, C, D, E) -> F
) = binding<F>(dependencies(v1, v2)) { f(v1.now, v2.now, v3.now, v4.now, v5.now) }