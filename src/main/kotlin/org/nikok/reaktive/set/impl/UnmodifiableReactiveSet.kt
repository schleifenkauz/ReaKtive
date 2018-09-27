/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.set.impl

import org.nikok.reaktive.InvalidationHandler
import org.nikok.reaktive.Observer
import org.nikok.reaktive.collection.*
import org.nikok.reaktive.set.*
import org.nikok.reaktive.set.binding.SetBinding
import org.nikok.reaktive.set.binding.binding
import org.nikok.reaktive.value.ReactiveValue
import org.nikok.reaktive.value.binding.Binding
import org.nikok.reaktive.value.reactiveValue

internal class UnmodifiableReactiveSet<E>(
    override val description: String, content: Iterable<E>
) : ReactiveSet<E> {
    override fun toString(): String = "$description: $now"

    override val now: Set<E> = content as? Set<E> ?: content.toSet()

    override fun observe(handler: CollectionChangeHandler<E, SetChange<E>>) = Observer.nothing

    override fun observe(handler: InvalidationHandler) = Observer.nothing

    override fun <F> map(newName: String, f: (E) -> F) = TODO()
    //unmodifiableReactiveSet(newName, now.map(f))

    override fun filter(newName: String, predicate: (E) -> Boolean): SetBinding<E> = TODO()
            //unmodifiableReactiveSet(newName, now.filter(predicate))

    override fun <F> flatMap(newName: String, f: (E) -> ReactiveCollection<F, *>): SetBinding<F> {
        return binding(newName, mutableSetOf()) {
            fun add(col: ReactiveCollection<F, *>) {
                addAll(col.now)
                val obs =
                        col.observe("Observer for $newName",
                                    removed = { _, e -> remove(e) },
                                    added = { _, e -> add(e) })
                addObserver(obs)
            }
            now.forEach { add(f(it)) }
        }
    }

    override fun minus(other: Collection<E>) = TODO()
    //unmodifiableReactiveSet("$description - $other", now.subtract(other))

    override fun minus(other: ReactiveCollection<E, *>): SetBinding<E> = TODO()

    override fun plus(other: Collection<E>): SetBinding<E> = TODO()
            //unmodifiableReactiveSet("$description + $other", now.union(other))

    override fun plus(other: ReactiveCollection<E, *>): SetBinding<E> = TODO()

    override fun <T> fold(name: String, initial: T, op: (T, E) -> T): Binding<T> {
        TODO()
        //return reactiveValue(name, now.fold(initial, op))
    }
}