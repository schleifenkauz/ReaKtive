/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value

import org.nikok.reaktive.Observer
import org.nikok.reaktive.Reactive
import org.nikok.reaktive.value.binding.Binding

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
    fun <F> map(newDescription: String, f: (T) -> F): Binding<F>

    /**
     * @return a [Binding] which is always bound to the value of [f] applied to the value of this [ReactiveValue]
     * * [flatMap] makes [ReactiveValue] an instance of Monad
     */
    fun <F> flatMap(newDescription: String, f: (T) -> ReactiveValue<F>): Binding<F>
}

