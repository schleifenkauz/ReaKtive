/**
 *@author Nikolaus Knop
 */

package reaktive.set.base

import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.collection.base.AbstractReactiveCollection
import reaktive.set.*
import reaktive.set.binding.Bindings
import reaktive.set.binding.SetBinding
import reaktive.value.binding.Binding

internal abstract class AbstractReactiveSet<out E> : ReactiveSet<E>, AbstractReactiveCollection<E, SetChange<E>>() {
    override fun observeSet(handler: (SetChange<E>) -> Unit): Observer = implObserve(handler)

    override fun <F> map(f: (E) -> F): SetBinding<F> = Bindings.map(this, f)

    override fun filter(predicate: (E) -> Boolean): SetBinding<E> =
        Bindings.filter(this, predicate)

    override fun <F> flatMap(f: (E) -> ReactiveCollection<F>): SetBinding<F> = Bindings.flatMap(this, f)

    override fun minus(other: Collection<Any?>): SetBinding<@UnsafeVariance E> =
        this - unmodifiableReactiveSet(other)

    override fun minus(other: ReactiveCollection<Any?>): SetBinding<@UnsafeVariance E> =
        Bindings.subtract(this, other)

    override fun plus(other: Collection<@UnsafeVariance E>): SetBinding<@UnsafeVariance E> =
        this + unmodifiableReactiveSet(other)

    override fun plus(other: ReactiveCollection<@UnsafeVariance E>): SetBinding<@UnsafeVariance E> =
        Bindings.union(listOf(this, other))

    override fun intersect(other: ReactiveSet<@UnsafeVariance E>) = Bindings.intersect(this, other)

    override fun intersect(other: Set<@UnsafeVariance E>): SetBinding<E> = intersect(unmodifiableReactiveSet(other))

    override fun <T> fold(initial: T, op: (T, E) -> T): Binding<T> = Bindings.fold(this, op)

    protected fun fireAdded(element: @UnsafeVariance E) {
        fireChange(SetChange.Added(element, this))
    }

    protected fun fireRemoved(element: @UnsafeVariance E) {
        fireChange(SetChange.Removed(element, this))
    }
}