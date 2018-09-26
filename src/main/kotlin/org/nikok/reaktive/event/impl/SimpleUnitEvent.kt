package org.nikok.reaktive.event.impl

import org.nikok.reaktive.event.UnitEvent

internal class SimpleUnitEvent(override val description: String) : UnitEvent {
    override fun fire(value: Unit) {
        stream.emit()
    }

    override fun fire() {
        fire(Unit)
    }

    override val stream = SimpleUnitEventStream(description)
}
