/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.event

import org.nikok.reaktive.Observer
import org.nikok.reaktive.Reactive
import org.nikok.reaktive.event.impl.InvalidationEventStream

/**
 * Subscribe to this [EventStream] with a [EventHandler] described with [name] handling events with [handler]
 */
fun <T, F> EventStream<T>.subscribe(name: String, handler: (EventStream<T>, T) -> F): Subscription {
    return subscribe(handler asEventHandler name)
}

/**
 * Syntactic sugar ignoring the fired event stream
 */
fun <T, F> EventStream<T>.subscribe(name: String, handler: (T) -> F): Subscription {
    return subscribe(handler asEventHandler name)
}

/**
 * @return an [EventHandler] with the specified [name] calling this function when a value was emitted
 */
infix fun <R, T> ((T) -> R).asEventHandler(name: String): EventHandler<T> =
        { _: EventStream<T>, value: T -> invoke(value) }.asEventHandler(name)

/**
 * @return an [EventHandler] with the specified [name] calling this function when a value was emitted
 */
infix fun <R, T> ((EventStream<T>, T) -> R).asEventHandler(name: String): EventHandler<T> {
    return object : EventHandler<T> {
        override fun handle(stream: EventStream<T>, value: T) {
            invoke(stream, value)
        }

        override val description: String = name
    }
}


/**
 * @return an [Observer] cancelling this [Subscription] when killed
*/
fun Subscription.asObserver(): Observer {
    return Observer(description) { cancel() }
}

/**
 * @return a [EventStream] of [Unit]s which emits when this [Reactive] is invalidated and is described by [description]
*/
fun Reactive.invalidated(description: String): EventStream<Unit> = InvalidationEventStream(this, description)

/**
 * @return a [EventStream] of [Unit]s with the default description which emits when this [Reactive] is invalidated
 */
val Reactive.invalidated: EventStream<Unit> get() = InvalidationEventStream(this)

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
@JvmName("subscribeBi") fun <A, B, F> BiEventStream<A, B>.subscribe(name: String, handler: (A, B) -> F): Subscription {
    return subscribe(name) { (first, second) -> handler(first, second) }
}

/**
 * Subscribe to this [BiEventStream] by calling handler when a [Triple] is emitted
 */
@JvmName("subscribeTri") fun <A, B, C, F> TriEventStream<A, B, C>.subscribe(
    name: String,
    handler: (A, B, C) -> F
): Subscription {
    return subscribe(name) { (first, second, third) -> handler(first, second, third) }
}

/**
 * @return a [Subscription] which kills this [Observer] when cancelled
*/
fun Observer.asSubscription(): Subscription {
    return Subscription(description, this::tryKill)
}