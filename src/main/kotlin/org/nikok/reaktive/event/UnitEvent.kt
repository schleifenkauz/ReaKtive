/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.event

interface UnitEvent: Event<Unit> {
    fun fire()
}