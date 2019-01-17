/**
 * @author Nikolaus Knop
 */

package reaktive.value

import reaktive.Observer
import reaktive.Reactive
import reaktive.value.binding.Binding

/**
 * Wraps a value of type [T] which can be observed for changes
 * ReactiveValue is a monad.
 * It can be mapped with [map] and there is [flatMap] operation
 */
interface ReactiveValue<out T> : Reactive, Value<T> {

    /**
     * Observe this [ReactiveValue] for changes invoking [handler] when the value changes
     * @return an [Observer] which when killed removes the [handler] from this [ReactiveValue]
     */
    fun observe(handler: ValueChangeHandler<T>): Observer

    /**
     * @return a [Binding] which always holds the value of [f] applied to the value of this [ReactiveValue]
     * * [map] makes [ReactiveValue] an instance of functor
     */
    fun <F> map(f: (T) -> F): Binding<F>

    /**
     * @return a [Binding] which is always bound to the value of [f] applied to the value of this [ReactiveValue]
     * * [flatMap] makes [ReactiveValue] an instance of Monad
     */
    fun <F> flatMap(f: (T) -> ReactiveValue<F>): Binding<F>
}

typealias ValueChangeHandler<T> = (ReactiveValue<T>, T, T) -> Unit