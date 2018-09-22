/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value.binding

import org.nikok.reaktive.Observer
import org.nikok.reaktive.value.*

/**
 * Observe [value] with a [ValueChangeHandler] described by [handlerDescription] using [handle] and
 * add the resulting observer to the observers of this [BindingBody]
 */
fun <T> BindingBody<T>.observe(
    value: ReactiveValue<T>, handlerDescription: String, handle: (ReactiveValue<T>, T, T) -> Unit
) {
    val obs = value.observe(handlerDescription, handle)
    addObserver(obs)
}

/**
 * Add all [observers] to this [BindingBody]
 */
fun <T> BindingBody<T>.addObservers(observers: Collection<Observer>) {
    observers.forEach(this::addObserver)
}

/**
 * Add all [observers] to this [BindingBody]
 */
fun <T> BindingBody<T>.addObservers(vararg observers: Observer) {
    addObservers(observers.asList())
}