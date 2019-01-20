/**
 * @author Nikolaus Knop
 */

package reaktive

/**
 * An object that can observed for invalidation
 *  * `equals` is implemented by referential equality, that means it is not overridden by implementing classes
 *  * `hashCode` is implemented with the memory address of the object,
 * that means it is not overridden by implementing classes
 */
interface Reactive {
    /**
     * Observes this [Reactive] calling [handler] when it is invalidated
     * When the [Observer] is killed or finalized the handler is removed
     */
    fun observe(handler: InvalidationHandler): Observer
}

typealias InvalidationHandler = (invalidated: Reactive) -> Unit