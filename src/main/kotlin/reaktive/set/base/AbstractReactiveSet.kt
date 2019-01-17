/**
 *@author Nikolaus Knop
 */

package reaktive.set.base

import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.collection.base.AbstractReactiveCollection
import reaktive.set.ReactiveSet
import reaktive.set.SetChange
import reaktive.set.binding.Bindings
import reaktive.set.binding.SetBinding
import reaktive.value.binding.Binding

internal abstract class AbstractReactiveSet<E> : ReactiveSet<E>, AbstractReactiveCollection<E, SetChange<E>>() {
    override fun observeSet(handler: (SetChange<E>) -> Unit): Observer = implObserve(handler)

    override fun <F> map(f: (E) -> F): SetBinding<F> = Bindings.map(this, f)

    override fun filter(predicate: (E) -> Boolean): SetBinding<E> =
        Bindings.filter(this, predicate)

    override fun <F> flatMap(f: (E) -> ReactiveCollection<F>): SetBinding<F> {
        TODO("not implemented")
    }

    override fun minus(other: Collection<@UnsafeVariance E>): SetBinding<@UnsafeVariance E> {
        TODO("not implemented")
    }

    override fun minus(other: ReactiveCollection<@UnsafeVariance E>): SetBinding<@UnsafeVariance E> {
        TODO("not implemented")
    }

    override fun plus(other: Collection<@UnsafeVariance E>): SetBinding<@UnsafeVariance E> {
        TODO("not implemented")
    }

    override fun plus(other: ReactiveCollection<@UnsafeVariance E>): SetBinding<@UnsafeVariance E> {
        TODO("not implemented")
    }

    override fun <T> fold(initial: T, op: (T, E) -> T): Binding<T> {
        TODO("not implemented")
    }

    protected fun fireAdded(element: E) {
        fireChange(SetChange.Added(element, this))
    }

    protected fun fireRemoved(element: E) {
        fireChange(SetChange.Removed(element, this))
    }
}