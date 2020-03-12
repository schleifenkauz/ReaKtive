/**
 *@author Nikolaus Knop
 */

package reaktive.list.impl

import reaktive.InvalidationHandler
import reaktive.Observer
import reaktive.collection.CollectionChange
import reaktive.collection.ReactiveCollection
import reaktive.list.ListChange
import reaktive.list.ReactiveList
import reaktive.list.binding.ListBinding
import reaktive.list.binding.asBinding
import reaktive.list.binding.impl.ListBindings
import reaktive.value.ReactiveValue
import reaktive.value.binding.*

internal class UnmodifiableReactiveList<E>(list: List<E>) : ReactiveList<E> {
    override val now: List<E> = list

    override fun observeList(handler: (ListChange<E>) -> Unit): Observer = Observer.nothing

    override fun <F> map(f: (E) -> F): ListBinding<F> = UnmodifiableReactiveList(now.map(f)).asBinding()

    override fun filter(predicate: (E) -> Boolean): ListBinding<E> =
        UnmodifiableReactiveList(now.filter(predicate)).asBinding()

    override fun plus(other: Collection<E>): ListBinding<E> = UnmodifiableReactiveList(now + other).asBinding()

    override fun plus(other: ReactiveCollection<E>): ListBinding<E> = ListBindings.concat(this, other)

    override fun minus(other: Collection<E>): ListBinding<E> = UnmodifiableReactiveList(now - other).asBinding()

    override fun minus(other: ReactiveCollection<E>): ListBinding<E> = ListBindings.subtract(this, other)

    override fun get(index: ReactiveValue<Int>): Binding<E?> = index.map { idx -> now.getOrNull(idx) }

    override fun observeCollection(handler: (CollectionChange<E>) -> Unit): Observer = Observer.nothing

    override fun <T> fold(initial: T, op: (T, E) -> T): Binding<T> = constantBinding(now.fold(initial, op))

    override fun <F> flatMap(f: (E) -> ReactiveCollection<F>): ListBinding<F> = ListBindings.flatMap(this, f)

    private val stringRepr by lazy { "UnmodifiableReactiveList($list)" }

    override fun toString(): String = stringRepr

    override fun observe(handler: InvalidationHandler): Observer = Observer.nothing
}