/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.set.impl

import org.nikok.reaktive.InvalidationHandler
import org.nikok.reaktive.Observer
import org.nikok.reaktive.collection.CollectionChangeHandler
import org.nikok.reaktive.collection.ReactiveCollection
import org.nikok.reaktive.set.*
import org.nikok.reaktive.value.ReactiveValue
import org.nikok.reaktive.value.reactiveValue

internal class UnmodifiableReactiveSet<E>(
    override val description: String, content: Iterable<E>
) : ReactiveSet<E> {
    override val now: Set<E> = content as? Set<E> ?: content.toSet()
    override fun observe(handler: CollectionChangeHandler<E, SetChange<E>>) = Observer.nothing

    override fun observe(handler: InvalidationHandler) = Observer.nothing

    override fun <F> map(newName: String, f: (E) -> F) = unmodifiableReactiveSet(newName, now.map(f))

    override fun filter(newName: String, predicate: (E) -> Boolean): ReactiveSet<E> =
            unmodifiableReactiveSet(newName, now.filter(predicate))

    override fun <F> flatMap(newName: String, f: (E) -> ReactiveCollection<F, *>): ReactiveSet<F> {
        TODO("not implemented")
    }

    override fun minus(other: Collection<E>) = unmodifiableReactiveSet("$description - $other", now.subtract(other))

    override fun minus(other: ReactiveCollection<E, *>): ReactiveSet<E> = TODO()

    override fun plus(other: Collection<E>): ReactiveSet<E> =
            unmodifiableReactiveSet("$description + $other", now.union(other))

    override fun plus(other: ReactiveCollection<E, *>): ReactiveSet<E> = TODO()

    override fun <T> fold(name: String, initial: T, op: (T, E) -> T): ReactiveValue<T> {
        return reactiveValue(name, now.fold(initial, op))
    }
}