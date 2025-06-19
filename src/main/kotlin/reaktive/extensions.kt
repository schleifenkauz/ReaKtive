/**
 * @author Nikolaus Knop
 */

package reaktive

import javafx.beans.value.ObservableValue
import reaktive.event.EventStream
import reaktive.value.ReactiveValue
import reaktive.value.binding.Binding
import reaktive.value.binding.binding
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * Syntactic sugar for observe { -> handler() }
 */
inline fun Reactive.observe(crossinline handler: () -> Unit) = observe { handler() }

/**
 * Return a [Binding] that is reevaluated on every invalidation of this [Reactive]
 * and supplies the function [f] with the receiver to compute the value.
 */
fun <F> Reactive.map(f: () -> F): Binding<F> = binding(this, f)

/**
 * Return a new [Observer] that kills all the given [Observer]s when killed.
 */
fun Iterable<Observer>.combined(): Observer = CompositeObserver(this)

infix fun Observer.and(other: Observer): Observer = listOf(this, other).combined()

/**
 * Store a weak reference to the given [referent] and call the given [handler]
 * with the referent as the receiver when this [ReactiveValue] changes.
 * When the [referent] is garbage collected the [handler] will never run again.
 */
fun <T, R : Any> ReactiveValue<T>.observe(
    referent: R,
    handler: R.(changed: ReactiveValue<T>, old: T, new: T) -> Unit
): Observer {
    val ref = WeakReference(referent)
    return observe { changed: ReactiveValue<T>, old: T, new: T ->
        ref.get()?.handler(changed, old, new)
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
    val ref = WeakReference(referent)
    return observe { stream: EventStream<T>, value: T ->
        ref.get()?.handler(stream, value)
    }
}

/**
 * Store a weak reference to the given [receiver] and call the given [listener]
 * with the referent as the receiver when this [ObservableValue] changes.
 * When the [receiver] is garbage collected the [listener] will never notified again.
 */
fun <R : Any, T : Any?> ObservableValue<T>.addListener(receiver: R, listener: R.(T) -> Unit) {
    val ref = WeakReference(receiver)
    addListener { _, _, newValue -> ref.get()?.listener(newValue) }
}

/**
 * Enables the property delegation syntax for [Reference]s.
 */
operator fun <T> Reference<T>.getValue(thisRef: Any?, property: KProperty<*>): T? = get()