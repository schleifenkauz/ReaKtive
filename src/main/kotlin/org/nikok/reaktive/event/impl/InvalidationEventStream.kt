/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.event.impl

import org.nikok.reaktive.Reactive
import org.nikok.reaktive.event.*
import org.nikok.reaktive.observe

internal class InvalidationEventStream(private val r: Reactive, description: String) : UnitEventStream(description) {
    constructor(r: Reactive): this(r, "Invalidation event stream for $r")

    override fun subscribe(handler: EventHandler<Unit>): Subscription {
        val subscription = r.observe(handler.description) { _ ->  handler.handle(this, Unit) }
        return subscription.asSubscription()
    }
}