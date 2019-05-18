package reaktive.set.impl

import reaktive.InvalidationHandler
import reaktive.Observer
import reaktive.collection.CollectionChange
import reaktive.collection.ReactiveCollection
import reaktive.set.*
import reaktive.set.binding.SetBinding
import reaktive.set.binding.unmodifiableSetBinding
import reaktive.value.binding.Binding
import reaktive.value.binding.constantBinding

internal object EmptyReactiveSet : SetBinding<Nothing> {
    override val now: Set<Nothing>
        get() = emptySet()

    override fun observeSet(handler: (SetChange<Nothing>) -> Unit): Observer = Observer.nothing

    override fun <F> map(f: (Nothing) -> F): SetBinding<F> = this

    override fun filter(predicate: (Nothing) -> Boolean): SetBinding<Nothing> = this

    override fun <F> flatMap(f: (Nothing) -> ReactiveCollection<F>): SetBinding<F> = this

    override fun minus(other: Collection<Nothing>): SetBinding<Nothing> = this

    override fun minus(other: ReactiveCollection<Nothing>): SetBinding<Nothing> = this

    override fun plus(other: Collection<Nothing>): SetBinding<Nothing> = unmodifiableSetBinding(other)

    override fun plus(other: ReactiveCollection<Nothing>): SetBinding<Nothing> = other.asSet()

    override fun intersect(other: ReactiveSet<Nothing>): SetBinding<Nothing> = this

    override fun intersect(other: Set<Nothing>): SetBinding<Nothing> = this

    override fun observeCollection(handler: (CollectionChange<Nothing>) -> Unit): Observer = Observer.nothing

    override fun <T> fold(initial: T, op: (T, Nothing) -> T): Binding<T> = constantBinding(initial)

    override fun observe(handler: InvalidationHandler): Observer = Observer.nothing

    override fun toString(): String = "{}"

    override fun dispose() {}
}
