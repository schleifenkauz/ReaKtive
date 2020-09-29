/**
 *@author Nikolaus Knop
 */

package reaktive.set

import reaktive.collection.CollectionChange

/**
 * Represents a change that was made to a [ReactiveSet]
 * @constructor
 */
sealed class SetChange<out E> : CollectionChange<E> {
    abstract override val modified: ReactiveSet<E>

    override val wasReplaced: Boolean
        get() = this is Updated

    override val wasAdded: Boolean
        get() = this is Added

    override val wasRemoved: Boolean
        get() = this is Removed

    val element: E
        get() = when (this) {
            is Added -> added
            is Removed -> removed
            is Updated -> updated
        }

    data class Added<E>(
        override val added: E,
        override val modified: ReactiveSet<E>
    ) : SetChange<E>() {
        override fun toString(): String = "added $added"
    }

    data class Removed<E>(
        override val removed: E,
        override val modified: ReactiveSet<E>
    ) : SetChange<E>() {
        override fun toString(): String = "removed $removed"
    }

    data class Updated<E>(val updated: E, override val modified: ReactiveSet<E>) : SetChange<E>() {
        override fun toString(): String = "updated $updated"

        override val added: E
            get() = updated
        override val removed: E
            get() = updated
    }
}