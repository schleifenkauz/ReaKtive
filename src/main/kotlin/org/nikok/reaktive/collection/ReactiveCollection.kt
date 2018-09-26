/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.collection

import org.nikok.reaktive.Observer
import org.nikok.reaktive.Reactive
import org.nikok.reaktive.value.ReactiveValue

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

    /**
     * @return A [ReactiveCollection] named [newName] which contains all elements from this collection mapped with [f]
    */
    fun <F> map(newName: String, f: (E) -> F): ReactiveCollection<F, *>

    /**
     * @return A [ReactiveCollection] named [newName] which contains all elements
     * from this collection which satisfy the given [predicate]
    */
    fun filter(newName: String, predicate: (E) -> Boolean): ReactiveCollection<E, C>

    /**
     * @return a flat view on this collection mapped with [f] named [newName]
     */
    fun <F> flatMap(newName: String, f: (E) -> ReactiveCollection<F, *>): ReactiveCollection<F, *>

    /**
     * @return a [ReactiveCollection] containing all elements from this collection not contained in [other]
    */
    fun minus(other: Collection<@UnsafeVariance E>): ReactiveCollection<E, C>

    /**
     * @return a [ReactiveCollection] containing all elements from this collection not contained in [other]
    */
    fun minus(other: ReactiveCollection<@UnsafeVariance E, *>): ReactiveCollection<E, C>
    /**
     * @return a [ReactiveCollection] containing all elements contained in this or in the [other] [Collection]
    */
    fun plus(other: Collection<@UnsafeVariance E>): ReactiveCollection<E, C>

    /**
     * @return a [ReactiveCollection] containing all elements contained in this or in the [other] [ReactiveCollection]
    */
    fun plus(other: ReactiveCollection<@UnsafeVariance E, *>): ReactiveCollection<E, C>

    /**
     * @return a [ReactiveValue] folding the elements of this [ReactiveCollection]
    */
    fun <T> fold(name: String, initial: T, op: (T, E) -> T): ReactiveValue<T>

    /**
     * @return a string with the [description] and the content of this [ReactiveCollection]
    */
    override fun toString(): String
}
