/**
 * @author Nikolaus Knop
 */

package reaktive.event

/**
 * An Event that can fire values of type [T]
 */
interface Event<T> {
    /**
     * Fires this event to [stream]
     */
    fun fire(value: T)

    /**
     * An event stream of events fired by this [Event]
     */
    val stream: EventStream<T>
}

typealias EventHandler<T> = (EventStream<T>, T) -> Unit