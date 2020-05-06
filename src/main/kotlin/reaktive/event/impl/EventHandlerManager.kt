/**
 *@author Nikolaus Knop
 */

package reaktive.event.impl

import reaktive.Observer
import reaktive.event.*
import reaktive.impl.HandlerCounter
import reaktive.impl.ObserverManager

internal class EventHandlerManager<T>(private val stream: EventStream<T>) {
    private val handlerCounter = HandlerCounter()
    private val observerManager = ObserverManager<EventHandler<T>>(handlerCounter)

    fun fire(value: T) {
        observerManager.notifyHandlers { eh -> eh(stream, value) }
    }

    fun observe(handler: EventHandler<T>): Observer {
        return observerManager.addHandler(handler)
    }
}