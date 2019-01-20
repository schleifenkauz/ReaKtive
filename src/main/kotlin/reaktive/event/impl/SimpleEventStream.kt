/**
 *@author Nikolaus Knop
 */

package reaktive.event.impl

import reaktive.event.AbstractEventStream

internal class SimpleEventStream<T> : AbstractEventStream<T>() {
    fun emit(value: T) {
        doEmit(value)
    }
}