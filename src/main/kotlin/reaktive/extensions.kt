/**
 * @author Nikolaus Knop
 */

package reaktive

import javafx.beans.value.ObservableValue
import org.nikok.kref.weak
import reaktive.event.EventStream
import reaktive.value.ReactiveValue
import reaktive.value.binding.Binding
import reaktive.value.binding.binding

/**
 * Syntactic sugar for observe { -> handler() }
 */
inline fun Reactive.observe(crossinline handler: () -> Unit) = observe { handler() }

/**
 * Return a [Binding] that is reevaluated on every invalidation of this [Reactive]
 * and supplies the function [f] with the receiver to compute the value.
 */
fun <R : Reactive, F> R.map(f: (R) -> F): Binding<F> = binding<F>(dependencies(this)) { f(this) }

/**
 * Return a new [Observer] that kills all the given [Observer]s when killed.
 */
fun Iterable<Observer>.combined() = Observer { forEach { it.kill() } }

/**
 * Store a weak reference to the given [referent] and call the given [handler]
 * with the referent as the receiver when this [ReactiveValue] changes.
 * When the [referent] is garbage collected the [handler] will never run again.
 */
fun <T, R : Any> ReactiveValue<T>.observe(
    referent: R,
    handler: R.(changed: ReactiveValue<T>, old: T, new: T) -> Unit
): Observer {
    val ref = weak(referent)
    return observe { changed: ReactiveValue<T>, old: T, new: T ->
        ref.referent?.handler(changed, old, new)
    }
}

/**
 * Store a weak reference to the given [referent] and call the given [handler]
 * with the referent as the receiver when this [EventStream] emits an event.
 * When the [referent] is garbage collected the [handler] will never run again.
 */
fun <T, R : Any> EventStream<T>.observe(
    referent: R,
    handler: R.(stream: EventStream<T>, value: T) -> Unit
): Observer {
    val ref = weak(referent)
    return observe { stream: EventStream<T>, value: T ->
        ref.referent?.handler(stream, value)
    }
}

/**
 * Store a weak reference to the given [receiver] and call the given [listener]
 * with the referent as the receiver when this [ObservableValue] changes.
 * When the [receiver] is garbage collected the [listener] will never notified again.
 */
fun <R : Any, T : Any?> ObservableValue<T>.addListener(receiver: R, listener: R.(T) -> Unit) {
    val ref = weak(receiver)
    addListener { _, _, newValue -> ref.referent?.listener(newValue) }
}