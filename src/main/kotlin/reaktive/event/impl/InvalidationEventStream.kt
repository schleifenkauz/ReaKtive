/**
 *@author Nikolaus Knop
 */

package reaktive.event.impl

import reaktive.Observer
import reaktive.Reactive
import reaktive.event.*

internal class InvalidationEventStream(private val r: Reactive) : UnitEventStream() {

    override fun observe(handler: EventHandler<Unit>): Observer {
        return r.observe { handler(this, Unit) }
    }
}