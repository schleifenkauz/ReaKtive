/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.event.impl

import org.nikok.reaktive.event.EventHandler
import org.nikok.reaktive.event.Subscription

internal class SimpleUnitEventStream(description: String) : UnitEventStream(description) {
    private val handlerManager by lazy { EventHandlerManager(this) }

    override fun subscribe(handler: EventHandler<Unit>): Subscription {
        return handlerManager.subscribe(handler)
    }

    fun emit() {
        handlerManager.fire(Unit)
    }
}