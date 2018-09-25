package org.nikok.reaktive.set

import org.nikok.reaktive.Observer
import org.nikok.reaktive.collection.CollectionChangeHandler
import org.nikok.reaktive.collection.ReactiveCollection

/**
 * A Set of elements which can be observed for modifications
*/
interface ReactiveSet<out E> : ReactiveCollection<E, SetChange<E>> {
    override fun observe(handler: CollectionChangeHandler<E, SetChange<E>>): Observer 

    override fun <F> map(newName: String, f: (E) -> F): ReactiveSet<F>

    override fun filter(newName: String, predicate: (E) -> Boolean): ReactiveSet<E>

    override fun <F> flatMap(newName: String, f: (E) -> ReactiveCollection<F, *>): ReactiveSet<F>

    override fun minus(other: Collection<@UnsafeVariance E>): ReactiveSet<E>

    override fun minus(other: ReactiveCollection<@UnsafeVariance E, *>): ReactiveSet<E>

    override fun plus(other: Collection<@UnsafeVariance E>): ReactiveSet<E>

    override fun plus(other: ReactiveCollection<@UnsafeVariance E, *>): ReactiveSet<E>
}
