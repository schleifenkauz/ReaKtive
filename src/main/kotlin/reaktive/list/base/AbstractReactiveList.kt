/**
 *@author Nikolaus Knop
 */

package reaktive.list.base

import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.collection.base.AbstractReactiveCollection
import reaktive.list.ListChange
import reaktive.list.ListChange.*
import reaktive.list.ReactiveList
import reaktive.list.binding.ListBinding
import reaktive.list.binding.listBinding
import reaktive.value.binding.Binding

internal abstract class AbstractReactiveList<out E> : ReactiveList<E>, AbstractReactiveCollection<E, ListChange<E>>() {
    override fun observeList(handler: (ListChange<E>) -> Unit): Observer = implObserve(handler)

    override fun <F> map(f: (E) -> F): ListBinding<F> = listBinding(now.map(f)) {
        val obs = observeCollection { ch ->
            when (ch) {
                is Replaced -> set(ch.index, f(ch.new))
                is Removed  -> removeAt(ch.index)
                is Added    -> add(f(ch.element))
            }
        }
        addObserver(obs)
    }

    override fun filter(predicate: (E) -> Boolean): ListBinding<E> =
        listBinding(now.filter(predicate)) {
            val obs = observeCollection { ch ->
                when (ch) {
                    is Replaced -> TODO()
                    is Removed  -> TODO()
                    is Added    -> TODO()
                }
            }
            addObserver(obs)
        }

    override fun plus(other: Collection<@UnsafeVariance E>): ListBinding<E> {
        TODO("not implemented")
    }

    override fun plus(other: ReactiveCollection<@UnsafeVariance E>): ListBinding<E> {
        TODO("not implemented")
    }

    override fun <F> flatMap(f: (E) -> ReactiveCollection<F>): ListBinding<F> {
        TODO("not implemented")
    }

    override fun minus(other: Collection<@UnsafeVariance E>): ListBinding<E> {
        TODO("not implemented")
    }

    override fun minus(other: ReactiveCollection<@UnsafeVariance E>): ListBinding<E> {
        TODO("not implemented")
    }

    override fun <T> fold(initial: T, op: (T, E) -> T): Binding<T> {
        TODO("not implemented")
    }
}