/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.event

import org.nikok.reaktive.Described

/**
 * An Event that can fire values of type [T]
*/
interface Event<T>: Described {
    /**
     * Fires this event to [stream]
    */
    fun fire(value: T)

    /**
     * An event stream of events fired by this [Event]
    */
    val stream: EventStream<T>
}