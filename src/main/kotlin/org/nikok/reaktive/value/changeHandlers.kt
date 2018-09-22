/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value

import kotlin.reflect.KFunction3

/**
 * @return a [ValueChangeHandler] described with [name] which calls [changed] on a value change ignoring the returned value
 */
fun <T, R> valueChangeHandler(name: String, changed: (ReactiveValue<T>, T, T) -> R): ValueChangeHandler<T> {
    return object : ValueChangeHandler<T> {
        override fun valueChanged(rv: ReactiveValue<T>, old: T, new: T) {
            changed(rv, old, new)
        }

        override val description: String = name
    }
}

/**
 * Syntactic sugar for `valueChangeHandler(desc, this)`
 */
infix fun <T, R> ((ReactiveValue<T>, T, T) -> R).withDescription(desc: String): ValueChangeHandler<T> =
        valueChangeHandler(desc, this)

/**
 * @return a [ValueChangeHandler] using the name of the reflected function
 * @throws IllegalArgumentException if the receiver is an anonymous function
 */
fun <T, F> (KFunction3<ReactiveValue<T>, T, T, F>).asValueChangeHandler() = valueChangeHandler(name, this)