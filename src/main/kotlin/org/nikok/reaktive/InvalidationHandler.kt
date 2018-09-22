/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive

/**
 * Is used to observe [Reactive]s for invalidation
 */
interface InvalidationHandler : Described {
    /**
     * Called when [invalidated] has been invalidated
     */
    fun invalidated(invalidated: Reactive)
}