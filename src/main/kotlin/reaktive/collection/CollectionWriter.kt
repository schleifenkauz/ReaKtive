package reaktive.collection

/**
 * Used to modify [ReactiveCollection]s
*/
interface CollectionWriter<E> {
    /**
     * Add the specified [element] to the associated collection
     * @return whether the element was added
    */
    fun add(element: E): Boolean

    /**
     * Remove the specified [element] from the associated collection
     * @return whether the element was removed
     */
    fun remove(element: E): Boolean

    /**
     * Add all [elements] to the associated collection
     * @return whether any elements were added
     */
    fun addAll(elements: Collection<E>): Boolean

    /**
     * Remove all [elements] from the associated collection
     * @return whether any elements were removed
    */
    fun removeAll(elements: Collection<E>): Boolean

    /**
     * Clear the associated collection
    */
    fun clear()

    /**
     * Execute [block] feeding the current content of the collection to it when it wasn't garbage collected before
    */
    fun <T> withContent(block: (MutableCollection<E>) -> T): T?
}
