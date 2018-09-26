/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.collection

import org.nikok.reaktive.Observer
import org.nikok.reaktive.value.ReactiveInt
import org.nikok.reaktive.value.binding.Binding
import org.nikok.reaktive.value.binding.binding

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

/**
 * @return a [Binding] computing the size of this [ReactiveCollection]
*/
val ReactiveCollection<*, *>.size: Binding<Int> get() {
    val desc = "size of $this"
    return binding(desc, now.size) {
        val obs = observe(object : SimpleCollectionChangeHandler<Any?>() {
            override fun added(rc: ReactiveCollection<Any?, *>, element: Any?) {
                withValue { set(it + 1) }
            }

            override fun removed(rc: ReactiveCollection<Any?, *>, element: Any?) {
                withValue { set(it - 1) }
            }

            override val description: String
                get() = "handler for $desc"
        })
        addObserver(obs)
    }
}