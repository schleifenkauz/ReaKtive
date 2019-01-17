/**
 * @author Nikolaus Knop
 */

package reaktive.collection

import reaktive.Observer
import reaktive.Reactive
import reaktive.collection.binding.CollectionBinding
import reaktive.value.binding.Binding

/**
 * A [Collection] of elements of type [E] which can be observed for changes
 */
interface ReactiveCollection<out E> : Reactive {
    /**
     * @return the content of this [ReactiveCollection] at this moment
     */
    val now: Collection<E>

    /**
     * Observe this collection for modifications invoking [handler] when the collection was modified
     * @return an [Observer] which when killed removes the [handler]
     */
    fun observeCollection(handler: (CollectionChange<E>) -> Unit): Observer

    /**
     * @return A [CollectionBinding] which contains all elements from this collection mapped with [f]
     */
    fun <F> map(f: (E) -> F): CollectionBinding<F>

    /**
     * @return A [CollectionBinding] which contains all elements
     * from this collection which satisfy the given [predicate]
     */
    fun filter(predicate: (E) -> Boolean): CollectionBinding<E>

    /**
     * @return a flat view on this collection mapped with [f]
     */
    fun <F> flatMap(f: (E) -> ReactiveCollection<F>): CollectionBinding<F>

    /**
     * @return a [CollectionBinding] containing all elements from this collection not contained in [other]
     */
    fun minus(other: Collection<@UnsafeVariance E>): CollectionBinding<E>

    /**
     * @return a [CollectionBinding] containing all elements from this collection not contained in [other]
     */
    fun minus(other: ReactiveCollection<@UnsafeVariance E>): CollectionBinding<E>

    /**
     * @return a [CollectionBinding] containing all elements contained in this or in the [other] [Collection]
     */
    fun plus(other: Collection<@UnsafeVariance E>): CollectionBinding<E>

    /**
     * @return a [CollectionBinding] containing all elements contained in this or in the [other] [CollectionBinding]
     */
    fun plus(other: ReactiveCollection<@UnsafeVariance E>): CollectionBinding<E>

    /**
     * @return a [Binding] folding the elements of this [ReactiveCollection]
     */
    fun <T> fold(initial: T, op: (T, E) -> T): Binding<T>

    override fun toString(): String
}
