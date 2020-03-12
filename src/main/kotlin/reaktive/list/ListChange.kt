package reaktive.list

import reaktive.collection.CollectionChange

sealed class ListChange<out E> : CollectionChange<E> {
    /**
     * @return the index at that the change happened
     */
    abstract val index: Int

    override val wasAdded: Boolean
        get() = this is Added

    override val wasRemoved: Boolean
        get() = this is Removed

    val wasReplaced: Boolean get() = this is Replaced

    data class Removed<E>(
        override val index: Int, override val element: E,
        override val modified: ReactiveList<E>
    ) : ListChange<E>()

    data class Added<E>(
        override val index: Int, override val element: E,
        override val modified: ReactiveList<E>
    ) : ListChange<E>()

     data class Replaced<E>(
        override val index: Int,
        override val modified: ReactiveList<E>,
        val old: E,
        val new: E
    ) : ListChange<E>() {
        override val element: E
            get() = new
    }
}
