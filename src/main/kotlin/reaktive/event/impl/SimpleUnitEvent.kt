package reaktive.event.impl

import reaktive.event.UnitEvent

internal class SimpleUnitEvent : UnitEvent {
    override fun fire(value: Unit) {
        stream.emit()
    }

    override fun fire() {
        fire(Unit)
    }

    override val stream = SimpleUnitEventStream()
}
