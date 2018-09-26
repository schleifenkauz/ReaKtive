/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.event.impl

import org.nikok.reaktive.event.*
import org.nikok.reaktive.impl.HandlerCounter
import org.nikok.reaktive.impl.ObserverManager

internal class EventHandlerManager<T>(private val stream: EventStream<T>) {
    private val handlerCounter = HandlerCounter()
    private val observerManager = ObserverManager<EventHandler<T>>(handlerCounter)

    fun fire(value: T) {
        observerManager.notifyHandlers { eh -> eh.handle(stream, value) }
    }

    fun subscribe(handler: EventHandler<T>): Subscription {
        return observerManager.addHandler(handler).asSubscription()
    }
}