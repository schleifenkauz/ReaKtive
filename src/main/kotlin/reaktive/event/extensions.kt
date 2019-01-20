/**
 * @author Nikolaus Knop
 */

package reaktive.event

import reaktive.Observer
import reaktive.Reactive
import reaktive.event.impl.InvalidationEventStream

inline fun <T> EventStream<T>.subscribe(crossinline handler: (T) -> Unit) =
    subscribe { _: EventStream<T>, value: T -> handler(value) }

/**
 * @return an [Observer] cancelling this [Subscription] when killed
 */
fun Subscription.asObserver(): Observer {
    return Observer { cancel() }
}

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
@JvmName("subscribeBi") fun <A, B, F> BiEventStream<A, B>.subscribe(handler: (A, B) -> F): Subscription {
    return subscribe { (first, second) -> handler(first, second) }
}

/**
 * Subscribe to this [BiEventStream] by calling handler when a [Triple] is emitted
 */
@JvmName("subscribeTri") fun <A, B, C, F> TriEventStream<A, B, C>.subscribe(
    handler: (A, B, C) -> F
): Subscription {
    return subscribe { (first, second, third) -> handler(first, second, third) }
}

/**
 * @return a [Subscription] which kills this [Observer] when cancelled
 */
fun Observer.asSubscription(): Subscription {
    return Subscription(this::tryKill)
}