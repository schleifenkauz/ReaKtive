/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.BindingBody
import reaktive.Observer
import reaktive.value.ReactiveValue
import reaktive.value.ValueChangeHandler

/**
 * Observe [value] with a [ValueChangeHandler] described by [handlerDescription] using [handle] and
 * add the resulting observer to the observers of this [ValueBindingBody]
 */
fun <T> BindingBody.observe(
    value: ReactiveValue<T>, handle: (ReactiveValue<T>, T, T) -> Unit
) {
    val obs = value.observe(handle)
    addObserver(obs)
}

/**
 * Add all [observers] to this [ValueBindingBody]
 */
fun BindingBody.addObservers(observers: Collection<Observer>) {
    observers.forEach(this::addObserver)
}

/**
 * Add all [observers] to this [ValueBindingBody]
 */
fun BindingBody.addObservers(vararg observers: Observer) {
    addObservers(observers.asList())
}