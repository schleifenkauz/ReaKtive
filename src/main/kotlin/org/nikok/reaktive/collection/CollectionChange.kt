/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.collection

/**
 * Represents a change that was made to a [ReactiveCollection] with elements of type [E]
*/
interface CollectionChange<out E> {
    /**
     * @return `true` if in this change an element was added to the collection
    */
    val wasAdded: Boolean

    /**
     * @return `true` if in this change an element was removed from the collection
    */
    val wasRemoved: Boolean

    /**
     * @return the element that was added or removed from the modified collection
    */
    val element: E

    /**
     * @return the modified collection
    */
    val modified: ReactiveCollection<E, *>
}