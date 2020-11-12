package reaktive.event

import reaktive.Observer
import reaktive.event.impl.EventHandlerManager
import reaktive.value.ReactiveValue
import reaktive.value.binding.createBinding

/**
 * Skeletal implementation for [EventStream]
 * @constructor
*/
abstract class AbstractEventStream<T> : EventStream<T> {
    private val handlerManager by lazy { EventHandlerManager(this) }

    override fun observe(handler: EventHandler<T>): Observer {
        return handlerManager.observe(handler)
    }

    override val lastFired: ReactiveValue<T?> = lastFired()

    /**
     * Emit the specified [value] from this event stream
    */
    protected fun doEmit(value: T) {
        handlerManager.fire(value)
    }

    private fun lastFired(): ReactiveValue<T?> {
        return createBinding<T?>(null) {
            val observer = observe { _, fired ->
                set(fired)
            }
            addObserver(observer)
        }
    }
}

