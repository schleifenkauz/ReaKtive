/**
 * @author Nikolaus Knop
 */

package reaktive.collection

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
     * @return `true` if an element was replaced in this change
    */
    val wasReplaced: Boolean

    /**
     * @return the element that was added in this change
     * @throws NoSuchElementException if no element was added
    */
    val added: E get() = throw NoSuchElementException()

    /**
     * @return the element that was removed in this change
     * @throws NoSuchElementException if no element was removed
    */
    val removed: E get() = throw NoSuchElementException()

    /**
     * @return the modified collection
    */
    val modified: ReactiveCollection<E>
}