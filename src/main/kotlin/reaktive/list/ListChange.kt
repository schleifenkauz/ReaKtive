package reaktive.list

import reaktive.collection.CollectionChange

sealed class ListChange<out E> : CollectionChange<E> {
    /**
     * @return the index at that the change happened
     */
    abstract val index: Int

    override val wasAdded: Boolean
        get() = this is Added || wasReplaced

    override val wasRemoved: Boolean
        get() = this is Removed || wasReplaced

    override val wasReplaced: Boolean get() = this is Replaced

    data class Removed<E>(
        override val index: Int, override val removed: E,
        override val modified: ReactiveList<E>
    ) : ListChange<E>() {
        override fun toString(): String = "remove $removed at index $index"
    }

    data class Added<E>(
        override val index: Int, override val added: E,
        override val modified: ReactiveList<E>
    ) : ListChange<E>() {
        override fun toString(): String = "added $added at $index"
    }

    data class Replaced<E>(
        override val index: Int,
        override val modified: ReactiveList<E>,
        override val removed: E,
        override val added: E
    ) : ListChange<E>() {
        override fun toString(): String = "replace element $removed at index $index by $added"
    }
}
