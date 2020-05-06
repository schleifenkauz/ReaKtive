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
        get() = false

    val element: E get() = when (this) {
        is Added   -> added
        is Removed -> removed
    }

    internal data class Added<E>(
        override val added: E,
        override val modified: ReactiveSet<E>
    ) : SetChange<E>() {
        override val wasAdded: Boolean
            get() = true
        override val wasRemoved: Boolean
            get() = false

        override fun toString(): String = "added $added to $modified"
    }

    internal data class Removed<E>(
        override val removed: E,
        override val modified: ReactiveSet<E>
    ) : SetChange<E>() {
        override val wasAdded: Boolean
            get() = false
        override val wasRemoved: Boolean
            get() = true

        override fun toString(): String = "removed $removed from $modified"
    }
}