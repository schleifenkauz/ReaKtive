/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.event

import org.nikok.reaktive.event.impl.SimpleEvent
import org.nikok.reaktive.event.impl.SimpleUnitEvent

fun <T> event(description: String): Event<T> = SimpleEvent(description)

fun unitEvent(description: String): UnitEvent = SimpleUnitEvent(description)

fun <T, R> biEvent(description: String): BiEvent<T, R> = event(description)

fun <A, B, C> triEvent(description: String): TriEvent<A, B, C> = event(description)