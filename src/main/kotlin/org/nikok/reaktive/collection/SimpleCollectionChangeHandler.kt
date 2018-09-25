/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.collection

/**
 * A simple [CollectionChangeHandler] which delegates changes to two methods [added] and [removed]
*/
abstract class SimpleCollectionChangeHandler<E>: CollectionChangeHandler<E, CollectionChange<E>> {
    /**
     * Called when [element] was added to [rc]
    */
    protected abstract fun added(rc: ReactiveCollection<E, *>, element: E)

    /**
     * Called when [element] was removed from [rc]
    */
    protected abstract fun removed(rc: ReactiveCollection<E, *>, element: E)

    override fun handle(change: CollectionChange<E>) {
        val modified = change.modified
        val element = change.element
        if (change.wasAdded) added(modified, element)
        else if (change.wasRemoved) removed(modified, element)
    }
}