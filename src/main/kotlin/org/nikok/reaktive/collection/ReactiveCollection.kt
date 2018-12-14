/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.collection

import kserial.*
import kserial.lens.SerialFormat
import kserial.lens.lens
import org.nikok.reaktive.Observer
import org.nikok.reaktive.Reactive
import org.nikok.reaktive.collection.binding.CollectionBinding
import org.nikok.reaktive.value.binding.Binding

/**
 * A [Collection] of elements of type [E] which can be observed for changes of type [C]
 */
interface ReactiveCollection<out E, out C : CollectionChange<E>> : Reactive, Serializable {
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
     * @return A [CollectionBinding] named [newName] which contains all elements from this collection mapped with [f]
     */
    fun <F> map(newName: String, f: (E) -> F): CollectionBinding<F, *>

    /**
     * @return A [CollectionBinding] named [newName] which contains all elements
     * from this collection which satisfy the given [predicate]
     */
    fun filter(newName: String, predicate: (E) -> Boolean): CollectionBinding<E, C>

    /**
     * @return a flat view on this collection mapped with [f] named [newName]
     */
    fun <F> flatMap(newName: String, f: (E) -> ReactiveCollection<F, *>): CollectionBinding<F, *>

    /**
     * @return a [CollectionBinding] containing all elements from this collection not contained in [other]
     */
    fun minus(other: Collection<@UnsafeVariance E>): CollectionBinding<E, C>

    /**
     * @return a [CollectionBinding] containing all elements from this collection not contained in [other]
     */
    fun minus(other: ReactiveCollection<@UnsafeVariance E, *>): CollectionBinding<E, C>

    /**
     * @return a [CollectionBinding] containing all elements contained in this or in the [other] [Collection]
     */
    fun plus(other: Collection<@UnsafeVariance E>): CollectionBinding<E, C>

    /**
     * @return a [CollectionBinding] containing all elements contained in this or in the [other] [CollectionBinding]
     */
    fun plus(other: ReactiveCollection<@UnsafeVariance E, *>): CollectionBinding<E, C>

    /**
     * @return a [Binding] folding the elements of this [ReactiveCollection]
     */
    fun <T> fold(name: String, initial: T, op: (T, E) -> T): Binding<T>

    /**
     * @return a string with the [description] and the content of this [ReactiveCollection]
     */
    override fun toString(): String

    override fun serialize(output: Output, context: SerialContext) {
        format.serialize(this, output, context)
    }

    override fun deserialize(input: Input, context: SerialContext) {
        format.deserialize(this, input, context)
    }

    companion object {
        private val format = SerialFormat(
            ReactiveCollection<*, *>::now.lens()
        )
    }
}
