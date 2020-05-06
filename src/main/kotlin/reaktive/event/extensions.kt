/**
 * @author Nikolaus Knop
 */

package reaktive.event

import reaktive.Observer
import reaktive.Reactive
import reaktive.event.impl.InvalidationEventStream

inline fun <T> EventStream<T>.observe(crossinline handler: (T) -> Unit) =
    observe { _: EventStream<T>, value: T -> handler(value) }

/**
 * @return a [EventStream] of [Unit]s which emits when this [Reactive] is invalidated and is described by [description]
 */
fun Reactive.invalidated(): EventStream<Unit> = InvalidationEventStream(this)

/**
 * @return a [EventStream] of [Unit]s with the default description which emits when this [Reactive] is invalidated
 */
val Reactive.onInvalidated: EventStream<Unit> get() = InvalidationEventStream(this)

/**
 * Fire a pair of [first] and [second]
 */
fun <A, B> BiEvent<A, B>.fire(first: A, second: B) {
    fire(first to second)
}

/**
 * Fire a triple of [first], [second] and [third]
 */
fun <A, B, C> TriEvent<A, B, C>.fire(first: A, second: B, third: C) {
    fire(Triple(first, second, third))
}

/**
 * Subscribe to this [BiEventStream] by calling handler when a [Pair] is emitted
 */
@JvmName("subscribeBi") fun <A, B, F> BiEventStream<A, B>.observe(handler: (A, B) -> F): Observer {
    return observe { _, (first, second) -> handler(first, second) }
}

/**
 * Subscribe to this [BiEventStream] by calling handler when a [Triple] is emitted
 */
@JvmName("subscribeTri") fun <A, B, C, F> TriEventStream<A, B, C>.observe(
    handler: (A, B, C) -> F
): Observer {
    return observe { _, (first, second, third) -> handler(first, second, third) }
}
