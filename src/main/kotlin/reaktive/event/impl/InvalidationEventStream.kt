/**
 *@author Nikolaus Knop
 */

package reaktive.event.impl

import reaktive.Reactive
import reaktive.event.*

internal class InvalidationEventStream(private val r: Reactive) : UnitEventStream() {

    override fun subscribe(handler: EventHandler<Unit>): Subscription {
        val subscription = r.observe { handler(this, Unit) }
        return subscription.asSubscription()
    }
}