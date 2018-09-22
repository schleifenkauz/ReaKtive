/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive

/**
 * An object that can observed for invalidation
 */
interface Reactive : Described {
    /**
     * Observes this [Reactive] calling [handler] when it is invalidated
     * When the [Observer] is killed or finalized the handler is removed
     */
    fun observe(handler: InvalidationHandler): Observer
}