package reaktive.event

import reaktive.event.impl.EventHandlerManager
import reaktive.value.ReactiveValue
import reaktive.value.binding.binding

/**
 * Skeletal implementation for [EventStream]
 * @constructor
*/
abstract class AbstractEventStream<T> : EventStream<T> {
    private val handlerManager by lazy { EventHandlerManager(this) }

    override fun subscribe(handler: EventHandler<T>): Subscription {
        return handlerManager.subscribe(handler)
    }

    override val lastFired: ReactiveValue<T?> = lastFired()

    /**
     * Emit the specified [value] from this event stream
    */
    protected fun doEmit(value: T) {
        handlerManager.fire(value)
    }

    private fun lastFired(): ReactiveValue<T?> {
        return binding<T?>(null) {
            val subscription = subscribe { fired ->
                set(fired)
            }
            addObserver(subscription.asObserver())
        }
    }
}

