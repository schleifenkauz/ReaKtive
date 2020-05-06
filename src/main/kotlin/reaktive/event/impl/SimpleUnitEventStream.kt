/**
 *@author Nikolaus Knop
 */

package reaktive.event.impl

import reaktive.Observer
import reaktive.event.EventHandler

internal class SimpleUnitEventStream : UnitEventStream() {
    private val handlerManager by lazy { EventHandlerManager(this) }

    override fun observe(handler: EventHandler<Unit>): Observer {
        return handlerManager.observe(handler)
    }

    fun emit() {
        handlerManager.fire(Unit)
    }
}