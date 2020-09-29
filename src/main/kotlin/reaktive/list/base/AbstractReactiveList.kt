/**
 *@author Nikolaus Knop
 */

package reaktive.list.base

import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.collection.base.AbstractReactiveCollection
import reaktive.dependencies
import reaktive.list.*
import reaktive.list.ListChange.*
import reaktive.list.binding.*
import reaktive.list.binding.impl.ListBindings
import reaktive.value.ReactiveValue
import reaktive.value.binding.Binding
import reaktive.value.binding.binding
import reaktive.value.now

internal abstract class AbstractReactiveList<out E> : ReactiveList<E>, AbstractReactiveCollection<E, ListChange<E>>() {
    override fun observeList(handler: (ListChange<E>) -> Unit): Observer = implObserve(handler)

    override fun <F> map(f: (E) -> F): ListBinding<F> = listBinding(now.map(f)) {
        val obs = observeCollection { ch ->
            when (ch) {
                is Replaced -> set(ch.index, f(ch.added))
                is Removed -> removeAt(ch.index)
                is Added -> add(ch.index, f(ch.added))
            }
        }
        addObserver(obs)
    }

    override fun filter(predicate: (E) -> Boolean): ListBinding<E> = FilterBinding(this, predicate)

    override fun plus(other: Collection<@UnsafeVariance E>): ListBinding<E> =
        ListBindings.concat(this, other.toReactiveList())

    override fun plus(other: ReactiveCollection<@UnsafeVariance E>): ListBinding<E> = ListBindings.concat(this, other)

    override fun <F> flatMap(f: (E) -> ReactiveCollection<F>): ListBinding<F> = ListBindings.flatMap(this, f)

    override fun minus(other: Collection<Any?>): ListBinding<E> = ListBindings.subtract(this, other.toReactiveList())

    override fun minus(other: ReactiveCollection<Any?>): ListBinding<E> = ListBindings.subtract(this, other)

    override fun <T> fold(initial: T, op: (T, E) -> T): Binding<T> = ListBindings.fold(this, initial, op)

    protected fun fireAdded(element: @UnsafeVariance E, index: Int) {
        fireChange(Added(index, element, this))
    }

    protected fun fireRemoved(element: @UnsafeVariance E, index: Int) {
        fireChange(Removed(index, element, this))
    }

    protected fun fireReplaced(old: @UnsafeVariance E, new: @UnsafeVariance E, index: Int) {
        fireChange(Replaced(index, this, old, new))
    }

    override fun get(index: ReactiveValue<Int>): Binding<E?> =
        binding<E?>(dependencies(this, index)) { now.getOrNull(index.now) }
}