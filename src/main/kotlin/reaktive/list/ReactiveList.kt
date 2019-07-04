/**
 * @author Nikolaus Knop
 */

package reaktive.list

import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.list.binding.ListBinding
import reaktive.value.ReactiveValue
import reaktive.value.binding.Binding

interface ReactiveList<out E> : ReactiveCollection<E> {
    override val now: List<E>

    fun observeList(handler: (ListChange<E>) -> Unit): Observer

    override fun <F> map(f: (E) -> F): ListBinding<F>

    override fun filter(predicate: (E) -> Boolean): ListBinding<E>

    override fun plus(other: Collection<@UnsafeVariance E>): ListBinding<E>

    override fun plus(other: ReactiveCollection<@UnsafeVariance E>): ListBinding<E>

    override fun <F> flatMap(f: (E) -> ReactiveCollection<F>): ListBinding<F>

    override fun minus(other: Collection<@UnsafeVariance E>): ListBinding<E>

    override fun minus(other: ReactiveCollection<@UnsafeVariance E>): ListBinding<E>

    /**
     * Returns a binding that always holds the element at the given index or `null` if there is no item at that index
    */
    operator fun get(index: ReactiveValue<Int>): Binding<E?>
}