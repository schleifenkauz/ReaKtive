/**
 *@author Nikolaus Knop
 */

package reaktive.set.impl

import reaktive.InvalidationHandler
import reaktive.Observer
import reaktive.collection.*
import reaktive.set.ReactiveSet
import reaktive.set.SetChange
import reaktive.set.binding.*
import reaktive.value.binding.Binding
import reaktive.value.binding.constantBinding

internal class UnmodifiableReactiveSet<E>(
    content: Iterable<E>
) : ReactiveSet<E> {
    override fun toString(): String = "UnmodifiableReactiveSet(now=$now)"

    override val now: Set<E> = content as? Set<E> ?: content.toSet()

    override fun observeCollection(handler: (CollectionChange<E>) -> Unit): Observer = Observer.nothing

    override fun observeSet(handler: (SetChange<E>) -> Unit): Observer = Observer.nothing

    override fun observe(handler: InvalidationHandler) = Observer.nothing

    override fun <F> map(f: (E) -> F) = unmodifiableSetBinding(now.map(f))

    override fun filter(predicate: (E) -> Boolean): SetBinding<E> = unmodifiableSetBinding(now.filter(predicate))

    override fun <F> flatMap(f: (E) -> ReactiveCollection<F>): SetBinding<F> {
        return setBinding(mutableSetOf()) {
            fun add(col: ReactiveCollection<F>) {
                addAll(col.now)
                val obs =
                    col.observeCollection(added = { _, e -> add(e) },
                        removed = { _, e -> remove(e) })
                addObserver(obs)
            }
            now.forEach { add(f(it)) }
        }
    }

    override fun minus(other: Collection<E>) = unmodifiableSetBinding(now - other)

    override fun minus(other: ReactiveCollection<E>): SetBinding<E> = Bindings.subtract(this, other)

    override fun plus(other: Collection<E>): SetBinding<E> = unmodifiableSetBinding(now + other)

    override fun plus(other: ReactiveCollection<E>): SetBinding<E> = Bindings.union(listOf(this, other))

    override fun intersect(other: ReactiveSet<E>) = Bindings.intersect(this, other)

    override fun intersect(other: Set<E>): SetBinding<E> = unmodifiableSetBinding(now.intersect(other))

    override fun <T> fold(initial: T, op: (T, E) -> T): Binding<T> = constantBinding(now.fold(initial, op))
}