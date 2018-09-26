/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.event.impl

import org.nikok.reaktive.event.AbstractEventStream

internal class SimpleEventStream<T>(description: String) : AbstractEventStream<T>(description) {
    fun emit(value: T) {
        doEmit(value)
    }
}