/**
 * @author Nikolaus Knop
 */

package reaktive.event

typealias BiEvent<A, B> = Event<Pair<A, B>>
typealias TriEvent<A, B, C> = Event<Triple<A, B, C>>

typealias BiEventStream<A, B> = EventStream<Pair<A, B>>
typealias TriEventStream<A, B, C> = EventStream<Triple<A, B, C>>