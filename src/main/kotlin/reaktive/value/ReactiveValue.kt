/**
 * @author Nikolaus Knop
 */

package reaktive.value

import reaktive.Observer
import reaktive.Reactive
import reaktive.impl.WeakReactive

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

    /**
     * @return a reference to this object that becomes weak if no listeners are registered
     */
    val weak: WeakReactive<ReactiveValue<T>>
}

typealias ValueChangeHandler<T> = (changed: ReactiveValue<T>, old: T, new: T) -> Unit