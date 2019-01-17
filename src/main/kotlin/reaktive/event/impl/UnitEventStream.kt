/**
 *@author Nikolaus Knop
 */

package reaktive.event.impl

import reaktive.event.EventStream
import reaktive.value.ReactiveValue
import reaktive.value.reactiveValue

internal abstract class UnitEventStream : EventStream<Unit> {
    override val lastFired: ReactiveValue<Unit> = reactiveValue(Unit)
}