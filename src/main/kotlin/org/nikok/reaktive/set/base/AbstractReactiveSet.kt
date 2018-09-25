/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.set.base

import org.nikok.reaktive.InvalidationHandler
import org.nikok.reaktive.Observer
import org.nikok.reaktive.collection.CollectionChangeHandler
import org.nikok.reaktive.collection.ReactiveCollection
import org.nikok.reaktive.set.ReactiveSet
import org.nikok.reaktive.set.SetChange
import org.nikok.reaktive.value.ReactiveValue

abstract class AbstractReactiveSet<out E> : ReactiveSet<E> {
    override fun observe(handler: CollectionChangeHandler<E, SetChange<E>>): Observer {
        TODO("not implemented")
    }

    override fun observe(handler: InvalidationHandler): Observer {
        TODO("not implemented")
    }

    override fun <F> map(newName: String, f: (E) -> F): ReactiveSet<F> {
        TODO("not implemented")
    }

    override fun filter(newName: String, predicate: (E) -> Boolean): ReactiveSet<E> {
        TODO("not implemented")
    }

    override fun <F> flatMap(newName: String, f: (E) -> ReactiveCollection<F, *>): ReactiveSet<F> {
        TODO("not implemented")
    }

    override fun minus(other: Collection<@UnsafeVariance E>): ReactiveSet<@UnsafeVariance E> {
        TODO("not implemented")
    }

    override fun minus(other: ReactiveCollection<@UnsafeVariance E, *>): ReactiveSet<@UnsafeVariance E> {
        TODO("not implemented")
    }

    override fun plus(other: Collection<@UnsafeVariance E>): ReactiveSet<@UnsafeVariance E> {
        TODO("not implemented")
    }

    override fun plus(other: ReactiveCollection<@UnsafeVariance E, *>): ReactiveSet<@UnsafeVariance E> {
        TODO("not implemented")
    }

    override fun <T> fold(name: String, initial: T, op: (T, @UnsafeVariance E) -> T): ReactiveValue<T> {
        TODO("not implemented")
    }
}