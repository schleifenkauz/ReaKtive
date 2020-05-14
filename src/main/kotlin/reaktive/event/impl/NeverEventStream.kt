/**
 *@author Nikolaus Knop
 */

package reaktive.event.impl

import reaktive.Observer
import reaktive.event.EventHandler
import reaktive.event.EventStream
import reaktive.value.ReactiveValue
import reaktive.value.reactiveValue

internal object NeverEventStream : EventStream<Nothing> {
    override fun observe(handler: EventHandler<Nothing>): Observer = Observer.nothing

    override val lastFired: ReactiveValue<Nothing?> = reactiveValue(null)
}