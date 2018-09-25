/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.collection

import org.nikok.reaktive.Observer

/**
 * Observe this [ReactiveCollection] with a [CollectionChangeHandler] named [handlerName] calling [handle]
 * @return the resulting [Observer]
*/
fun <E> ReactiveCollection<E, *>.observe(handlerName: String, handle: (CollectionChange<E>) -> Unit): Observer {
    val handler = collectionChangeHandler(handlerName, handle)
    TODO()
}

/**
 * Observers this [ReactiveCollection] with a [CollectionChangeHandler] named [handlerName] calling
 * [removed] when an element was removed and [added] when an element was added
 * @return the resulting [Observer]
*/
fun <E> ReactiveCollection<E, *>.observe(
    handlerName: String,
    added: (ReactiveCollection<E, *>, E) -> Unit,
    removed: (ReactiveCollection<E, *>, E) -> Unit
): Observer {
    val handler = collectionChangeHandler(handlerName, added, removed)
    TODO()
}