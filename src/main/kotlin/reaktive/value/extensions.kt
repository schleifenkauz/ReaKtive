/**
 * @author Nikolaus Knop
 */

package reaktive.value

import reaktive.Observer

/**
 * Syntactic sugar for `observe(handlerName) { _, old, new -> handle(old, new) }`
 */
inline fun <T, R> ReactiveValue<T>.observe(crossinline handle: (T, T) -> R): Observer =
    observe { _, old, new -> handle(old, new) }

/**
 * Syntactic sugar for `observe(handlerName) { _, new -> handle(new) }`
 */
inline fun <T, R> ReactiveValue<T>.observe(crossinline handle: (T) -> R): Observer =
    observe { _, new -> handle(new) }

/**
 * @return the wrapped value at this moment
 */
val <T> ReactiveValue<T>.now: T
    get() = get()

/**
 * Executes the given handler ones with the current value and registers it as an observer
 */
inline fun <T, R> ReactiveValue<T>.forEach(crossinline handle: (T) -> R): Observer {
    handle(now)
    return observe(handle)
}