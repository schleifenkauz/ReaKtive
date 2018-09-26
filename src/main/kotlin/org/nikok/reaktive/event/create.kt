/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.event

import org.nikok.reaktive.event.impl.SimpleEvent
import org.nikok.reaktive.event.impl.SimpleUnitEvent

/**
 * @return an [Event] described with [description]
*/
fun <T> event(description: String): Event<T> = SimpleEvent(description)

/**
 * @return a [UnitEvent] described with [description]
*/
fun unitEvent(description: String): UnitEvent = SimpleUnitEvent(description)

/**
 * @return a [BiEvent] described with [description]
*/
fun <T, R> biEvent(description: String): BiEvent<T, R> = event(description)

/**
 * @return a [TriEvent] described with [description]
*/
fun <A, B, C> triEvent(description: String): TriEvent<A, B, C> = event(description)