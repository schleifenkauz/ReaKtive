/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.event.impl

import org.nikok.reaktive.event.EventStream
import org.nikok.reaktive.value.ReactiveValue
import org.nikok.reaktive.value.reactiveValue

internal abstract class UnitEventStream(override val description: String) : EventStream<Unit> {
    override val lastFired: ReactiveValue<Unit> = reactiveValue("last fired of $this", Unit)
}