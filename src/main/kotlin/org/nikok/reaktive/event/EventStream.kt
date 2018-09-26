package org.nikok.reaktive.event

import org.nikok.reaktive.Described
import org.nikok.reaktive.value.ReactiveValue

/**
 * A stream of event values fired by a [Event]
 */
interface EventStream<out T>: Described {
    /**
     * Subscribes to this event
     * Invoking handler when a new value is fired
    */
    fun subscribe(handler: EventHandler<T>): Subscription

    /**
     * A [ReactiveValue] containing the last value that was emitted by this [EventStream] or `null`
     * if this event stream hasn't emitted any events
    */
    val lastFired: ReactiveValue<T?>
}
