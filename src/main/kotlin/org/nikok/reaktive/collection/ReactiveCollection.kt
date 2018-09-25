/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.collection

import org.nikok.reaktive.Observer
import org.nikok.reaktive.Reactive

/**
 * A [Collection] of elements of type [E] which can be observed for changes of type [C]
 */
interface ReactiveCollection<out E, out C : CollectionChange<E>> : Reactive {
    /**
     * @return the content of this [ReactiveCollection] at this moment
    */
    val now: Collection<E>

    /**
     * Observe this collection for modifications invoking [handler] when the collection was modified
     * @return an [Observer] which when killed removes the [handler]
    */
    fun observe(handler: CollectionChangeHandler<E, C>): Observer

    //Transformations
    /**
     * @return
    */
    fun <F> map(newName: String, f: (E) -> F): ReactiveCollection<F, *>

    /**
     * @return
    */
    fun filter(newName: String, f: (E) -> Boolean)
}