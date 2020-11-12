/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.Reactive
import reaktive.dependencies
import reaktive.value.*
import reaktive.value.binding.impl.AutoDependenciesBinding
import reaktive.value.binding.impl.BindingImpl

/**
 * Return a [Binding] initially set to [initialValue] and set up the binding by executing the given [block].
 */
inline fun <T> createBinding(initialValue: T, block: ValueBindingBody<T>.() -> Unit): Binding<T> =
    BindingImpl.newBinding(initialValue, block)

/**
 * Return a [Binding] which is recomputed if one of the [dependencies] is invalidated
 * @param compute the function the is used to compute the value of the returned [Binding]
 */
inline fun <T> binding(dependencies: Reactive, crossinline compute: () -> T): Binding<T> =
    createBinding(compute()) {
        observe(dependencies) {
            set(compute())
        }
    }

/**
 * Return a new [Binding] that determines its dependencies by itself.
 */
fun <T> binding(compute: AutoDependenciesBindingBody.() -> T): Binding<T> = AutoDependenciesBinding(compute)

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

/**
 * Create a new binding with the dependencies [v1] and [v2] computing the value by calling [f] with the current values.
 */
fun <A, B, C> binding(v1: ReactiveValue<A>, v2: ReactiveValue<B>, f: (A, B) -> C) =
    binding(dependencies(v1, v2)) { f(v1.now, v2.now) }

/**
 * Create a new binding with the dependencies [v1], [v2]  and[v3]
 * computing the value by calling [f] with the current values.
 */
fun <A, B, C, D> binding(v1: ReactiveValue<A>, v2: ReactiveValue<B>, v3: ReactiveValue<C>, f: (A, B, C) -> D) =
    binding(dependencies(v1, v2)) { f(v1.now, v2.now, v3.now) }

/**
 * Create a new binding with the dependencies [v1], [v2], [v3] and [v4]
 * computing the value by calling [f] with the current values.
 */
fun <A, B, C, D, E> binding(
    v1: ReactiveValue<A>,
    v2: ReactiveValue<B>,
    v3: ReactiveValue<C>,
    v4: ReactiveValue<D>,
    f: (A, B, C, D) -> E
) = binding(dependencies(v1, v2)) { f(v1.now, v2.now, v3.now, v4.now) }

/**
 * Create a new binding with the dependencies [v1], [v2], [v3], [v4] and [v5]
 * computing the value by calling [f] with the current values.
 */
fun <A, B, C, D, E, F> binding(
    v1: ReactiveValue<A>,
    v2: ReactiveValue<B>,
    v3: ReactiveValue<C>,
    v4: ReactiveValue<D>,
    v5: ReactiveValue<E>,
    f: (A, B, C, D, E) -> F
) = binding(dependencies(v1, v2)) { f(v1.now, v2.now, v3.now, v4.now, v5.now) }