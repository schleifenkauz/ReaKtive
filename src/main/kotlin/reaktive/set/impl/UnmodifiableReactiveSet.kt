/**
 *@author Nikolaus Knop
 */

package reaktive.set.impl

import reaktive.InvalidationHandler
import reaktive.Observer
import reaktive.collection.*
import reaktive.set.ReactiveSet
import reaktive.set.SetChange
import reaktive.set.binding.SetBinding
import reaktive.set.binding.setBinding
import reaktive.value.binding.Binding

internal class UnmodifiableReactiveSet<E>(
    content: Iterable<E>
) : ReactiveSet<E> {
    override fun toString(): String = "UnmodifiableReactiveSet(now=$now)"

    override val now: Set<E> = content as? Set<E> ?: content.toSet()

    override fun observeCollection(handler: (CollectionChange<E>) -> Unit): Observer = Observer.nothing

    override fun observeSet(handler: (SetChange<E>) -> Unit): Observer = Observer.nothing

    override fun observe(handler: InvalidationHandler) = Observer.nothing

    override fun <F> map(f: (E) -> F) = TODO()
    //unmodifiableReactiveSet(newName, now.map(f))

    override fun filter(predicate: (E) -> Boolean): SetBinding<E> = TODO()
    //unmodifiableReactiveSet(newName, now.filter(predicate))

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

    override fun minus(other: Collection<E>) = TODO()
    //unmodifiableReactiveSet("$description - $other", now.subtract(other))

    override fun minus(other: ReactiveCollection<E>): SetBinding<E> = TODO()

    override fun plus(other: Collection<E>): SetBinding<E> = TODO()
    //unmodifiableReactiveSet("$description + $other", now.union(other))

    override fun plus(other: ReactiveCollection<E>): SetBinding<E> = TODO()

    override fun intersect(other: ReactiveSet<E>) = TODO()

    override fun intersect(other: Set<E>): SetBinding<E> = TODO()

    override fun <T> fold(initial: T, op: (T, E) -> T): Binding<T> {
        TODO()
        //return reactiveValue(name, now.fold(initial, op))
    }
}