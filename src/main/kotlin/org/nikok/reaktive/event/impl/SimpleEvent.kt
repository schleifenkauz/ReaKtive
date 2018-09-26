/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.event.impl

import org.nikok.reaktive.event.Event

internal class SimpleEvent<T>(override val description: String) : Event<T> {
    override fun fire(value: T) {
        stream.emit(value)
    }

    override val stream = SimpleEventStream<T>("Event stream for event $description")
}