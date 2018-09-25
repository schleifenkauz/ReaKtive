/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value

import org.nikok.reaktive.Observer

/**
 * Observe this [ReactiveValue] with a [ValueChangeHandler] with name [handlerName] handling value changes with [handle]
 * @return the resulting [Observer]
 */
fun <T, R> ReactiveValue<T>.observe(handlerName: String, handle: (ReactiveValue<T>, T, T) -> R): Observer =
        observe(valueChangeHandler(handlerName, handle))

/**
 * Syntactic sugar for `observe(handlerName) { _, old, new -> handle(old, new) }`
 */
fun <T, R> ReactiveValue<T>.observe(handlerName: String, handle: (T, T) -> R): Observer =
        observe(handlerName) { _, old, new -> handle(old, new) }

/**
 * Syntactic sugar for `observe(handlerName) { _, new -> handle(new) }`
 */
fun <T, R> ReactiveValue<T>.observe(handlerName: String, handle: (T) -> R): Observer =
        observe(handlerName) { _, new -> handle(new) }

/**
 * @return the wrapped value at this moment
 */
val <T> ReactiveValue<T>.now: T
    get() = get()

