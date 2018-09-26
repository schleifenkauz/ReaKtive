/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.event

/**
 * A [Event] which only fires values of [Unit]
*/
interface UnitEvent: Event<Unit> {
    /**
     * Fire this [UnitEvent]
    */
    fun fire()
}