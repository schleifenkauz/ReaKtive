/**
 * @author Nikolaus Knop
 */

package reaktive.event

import reaktive.event.impl.*

/**
 * @return a new [Event]
 */
fun <T> event(): Event<T> = SimpleEvent()

/**
 * @return a new [UnitEvent]
 */
fun unitEvent(): UnitEvent = SimpleUnitEvent()

/**
 * @return a new [BiEvent]
 */
fun <T, R> biEvent(): BiEvent<T, R> = event()

/**
 * @return a new [TriEvent]
 */
fun <A, B, C> triEvent(): TriEvent<A, B, C> = event()

/**
 * @return an [EventStream] that never emits events
 */
fun <T> never(): EventStream<T> = NeverEventStream