/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.event.impl

import kserial.*
import org.nikok.reaktive.event.Event

internal class SimpleEvent<T>(override val description: String) : Event<T> {
    override fun fire(value: T) {
        stream.emit(value)
    }

    override val stream = SimpleEventStream<T>("Event stream for event $description")

    companion object : Serializer<SimpleEvent<*>> {
        override fun deserialize(cls: Class<SimpleEvent<*>>, input: Input, context: SerialContext): SimpleEvent<*> {
            val desc = input.readString()
            return SimpleEvent<Any>(desc)
        }

        override fun serialize(obj: SimpleEvent<*>, output: Output, context: SerialContext) {
            output.writeString(obj.description)
        }
    }
}