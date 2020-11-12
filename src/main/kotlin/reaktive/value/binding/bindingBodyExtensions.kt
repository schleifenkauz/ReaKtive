/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.*
import reaktive.value.ReactiveValue

/**
 * Observe the given [reactive] with the specified handler and
 * add the resulting observer to the observers of this [ValueBindingBody]
 */
fun <R : Reactive> BindingBody.observe(reactive: R, handle: () -> Unit): Observer {
    val obs = reactive.observe(handle)
    addObserver(obs)
    return obs
}

/**
 * Observe the given [value] with the specified handler and
 * add the resulting observer to the observers of this [ValueBindingBody]
 */
fun <T> BindingBody.observe(value: ReactiveValue<T>, handle: (ReactiveValue<T>, T, T) -> Unit): Observer {
    val obs = value.observe(handle)
    addObserver(obs)
    return obs
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