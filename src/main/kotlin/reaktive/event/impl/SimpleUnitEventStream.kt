/**
 *@author Nikolaus Knop
 */

package reaktive.event.impl

import reaktive.event.EventHandler
import reaktive.event.Subscription

internal class SimpleUnitEventStream : UnitEventStream() {
    private val handlerManager by lazy { EventHandlerManager(this) }

    override fun subscribe(handler: EventHandler<Unit>): Subscription {
        return handlerManager.subscribe(handler)
    }

    fun emit() {
        handlerManager.fire(Unit)
    }
}