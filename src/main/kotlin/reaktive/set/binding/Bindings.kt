package reaktive.set.binding

import reaktive.collection.observeCollection
import reaktive.set.ReactiveSet

internal object Bindings {
    fun <E, F> map(set: ReactiveSet<E>, f: (E) -> F): SetBinding<F> =
        setBinding(set.now.mapTo(mutableSetOf(), f)) {
            set.observeCollection(
                added = { _, e -> remove(f(e)) },
                removed = { _, e -> add(f(e)) }
                ).let(::addObserver)
            }

    fun <E> filter(set: ReactiveSet<E>, pred: (E) -> Boolean): SetBinding<E> =
        setBinding(set.now.filterTo(mutableSetOf(), pred)) {
            set.observeCollection(
                    added = { _, e -> if (pred(e)) add(e) },
                    removed = { _, e -> remove(e) }
                ).let(::addObserver)
            }
}