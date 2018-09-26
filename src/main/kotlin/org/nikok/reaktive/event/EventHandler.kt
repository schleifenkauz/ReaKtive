/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.event

import org.nikok.reaktive.Described

/**
 * Handles events fired by event streams
*/
interface EventHandler<in T>: Described {
    /**
     * Called when [stream] has fired [value]
    */
    fun handle(stream: EventStream<T>, value: T)
}