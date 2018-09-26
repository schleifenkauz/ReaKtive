/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.event

import org.nikok.reaktive.Observer
import org.nikok.reaktive.Reactive
import org.nikok.reaktive.event.impl.InvalidationEventStream

fun <T, F> EventStream<T>.subscribe(name: String, handler: (EventStream<T>, T) -> F): Subscription {
    return subscribe(handler asEventHandler name)
}

fun <T, F> EventStream<T>.subscribe(name: String, handler: (T) -> F): Subscription {
    return subscribe(handler asEventHandler name)
}

infix fun <R, T> ((T) -> R).asEventHandler(name: String): EventHandler<T> {
    return object : EventHandler<T> {
        override fun handle(stream: EventStream<T>, value: T) {
            invoke(value)
        }

        override val description: String = name
    }
}

infix fun <R, T> ((EventStream<T>, T) -> R).asEventHandler(name: String): EventHandler<T> {
    return object : EventHandler<T> {
        override fun handle(stream: EventStream<T>, value: T) {
            invoke(stream, value)
        }

        override val description: String = name
    }
}


fun Subscription.asObserver(): Observer {
    return Observer(description) { cancel() }
}

fun Reactive.invalidated(description: String): EventStream<Unit> = InvalidationEventStream(this, description)

val Reactive.invalidated: EventStream<Unit> get() = InvalidationEventStream(this)

fun <A, B> BiEvent<A, B>.fire(first: A, second: B) {
    fire(first to second)
}

fun <A, B, C> TriEvent<A, B, C>.fire(first: A, second: B, third: C) {
    fire(Triple(first, second, third))
}

@JvmName("subscribeBi")
fun <A, B, F> BiEventStream<A, B>.subscribe(name: String, handler: (A, B) -> F): Subscription {
    return subscribe(name) { (first, second) -> handler(first, second) }
}

@JvmName("subscribeTri")
fun <A, B, C, F> TriEventStream<A, B, C>.subscribe(name: String, handler: (A, B, C) -> F): Subscription {
    return subscribe(name) { (first, second, third) -> handler(first, second, third) }
}

fun Observer.asSubscription(): Subscription {
    return Subscription(description, this::tryKill)
}