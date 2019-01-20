/**
 *@author Nikolaus Knop
 */

package reaktive.event.impl

import reaktive.event.Event

internal class SimpleEvent<T> : Event<T> {
    override fun fire(value: T) {
        stream.emit(value)
    }

    override val stream = SimpleEventStream<T>()
}