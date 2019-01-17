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

    internal data class Added<E>(
        override val element: E,
        override val modified: ReactiveSet<E>
    ) : SetChange<E>() {
        override val wasAdded: Boolean
            get() = true
        override val wasRemoved: Boolean
            get() = false

        override fun toString(): String = "added $element to $modified"
    }

    internal data class Removed<E>(
        override val element: E,
        override val modified: ReactiveSet<E>
    ) : SetChange<E>() {
        override val wasAdded: Boolean
            get() = false
        override val wasRemoved: Boolean
            get() = true

        override fun toString(): String = "removed $element from $modified"
    }
}