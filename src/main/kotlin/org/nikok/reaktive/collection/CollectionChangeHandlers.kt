/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.collection

/**
 * @return a [CollectionChangeHandler] named [handlerName] handling changes by calling [handle]
*/
fun <E> collectionChangeHandler(
    handlerName: String,
    handle: (CollectionChange<E>) -> Unit
): CollectionChangeHandler<E, CollectionChange<E>> = object : CollectionChangeHandler<E, CollectionChange<E>> {
    override fun handle(change: CollectionChange<E>) {
        handle(change)
    }

    override val description: String = handlerName
}

/**
 * @return a [CollectionChangeHandler] named [handlerName]
 * calling [added] when an element was added and [removed] when an element was removed
*/
fun <E> collectionChangeHandler(
    handlerName: String,
    added: (ReactiveCollection<E, *>, E) -> Unit,
    removed: (ReactiveCollection<E, *>, E) -> Unit
): CollectionChangeHandler<E, *> = object : SimpleCollectionChangeHandler<E>() {
    override fun added(rc: ReactiveCollection<E, *>, element: E) {
        added.invoke(rc, element)
    }

    override fun removed(rc: ReactiveCollection<E, *>, element: E) {
        removed.invoke(rc, element)
    }

    override val description: String = handlerName
}