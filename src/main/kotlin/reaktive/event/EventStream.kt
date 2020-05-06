package reaktive.event

import reaktive.*
import reaktive.value.ReactiveValue

/**
 * A stream of event values fired by a [Event]
 */
interface EventStream<out T>: Reactive {
    /**
     * Subscribes to this event
     * Invoking handler when a new value is fired
     */
    fun observe(handler: EventHandler<T>): Observer

    override fun observe(handler: InvalidationHandler): Observer {
        TODO("not implemented")
    }

    /**
     * A [ReactiveValue] containing the last value that was emitted by this [EventStream] or `null`
     * if this event stream hasn't emitted any events
    */
    val lastFired: ReactiveValue<T?>
}
