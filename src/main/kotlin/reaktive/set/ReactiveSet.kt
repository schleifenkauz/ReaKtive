package reaktive.set

import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.set.binding.SetBinding

/**
 * A Set of elements which can be observed for modifications
 */
interface ReactiveSet<out E> : ReactiveCollection<E> {
    override val now: Set<E>

    fun observeSet(handler: (SetChange<E>) -> Unit): Observer

    override fun <F> map(f: (E) -> F): SetBinding<F>

    override fun filter(predicate: (E) -> Boolean): SetBinding<E>

    override fun <F> flatMap(f: (E) -> ReactiveCollection<F>): SetBinding<F>

    override fun minus(other: Collection<@UnsafeVariance E>): SetBinding<E>

    override fun minus(other: ReactiveCollection<@UnsafeVariance E>): SetBinding<E>

    override fun plus(other: Collection<@UnsafeVariance E>): SetBinding<E>

    override fun plus(other: ReactiveCollection<@UnsafeVariance E>): SetBinding<E>

    fun intersect(other: ReactiveSet<@UnsafeVariance E>): SetBinding<E>

    fun intersect(other: Set<@UnsafeVariance E>): SetBinding<E>
}
