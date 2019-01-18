/**
 * @author Nikolaus Knop
 */

package reaktive.value

import reaktive.Observer
import reaktive.Reactive

/**
 * Wraps a value of type [T] which can be observed for changes
 * ReactiveValue is a monad.
 * It can be mapped with [map] and there is a [flatMap] operation
 */
interface ReactiveValue<out T> : Reactive, Value<T> {

    /**
     * Observe this [ReactiveValue] for changes invoking [handler] when the value changes
     * @return an [Observer] which when killed removes the [handler] from this [ReactiveValue]
     */
    fun observe(handler: ValueChangeHandler<T>): Observer
}

typealias ValueChangeHandler<T> = (ReactiveValue<T>, T, T) -> Unit