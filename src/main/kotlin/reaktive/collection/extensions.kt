/**
 * @author Nikolaus Knop
 */

package reaktive.collection

import reaktive.Observer

/**
 * Observers this [ReactiveCollection] with a handler calling
 * [removed] when an element was removed and [added] when an element was added
 * @return the resulting [Observer]
 */
inline fun <E> ReactiveCollection<E>.observeCollection(
    crossinline added: (ReactiveCollection<E>, E) -> Unit,
    crossinline removed: (ReactiveCollection<E>, E) -> Unit
): Observer {
    val handler = { ch: CollectionChange<E> ->
        if (ch.wasAdded) added(ch.modified, ch.element)
        else if (ch.wasRemoved) removed(ch.modified, ch.element)
    }
    return observeCollection(handler)
}

