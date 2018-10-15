package org.nikok.reaktive.set.binding

import org.nikok.reaktive.collection.observe
import org.nikok.reaktive.set.ReactiveSet

internal object Bindings {
    fun <E, F> map(set: ReactiveSet<E>, newDescription: String, f: (E) -> F): SetBinding<F> =
            setBinding(newDescription, set.now.mapTo(mutableSetOf(), f)) {
        set.observe(
            "Observe for map binding",
            removed = { _, e -> add(f(e)) },
            added = { _, e -> remove(f(e)) }
        )
    }

    fun <E> filter(set: ReactiveSet<E>, newDescription: String, pred: (E) -> Boolean): SetBinding<E> =
            setBinding(newDescription, set.now.filterTo(mutableSetOf(), pred)) {
        set.observe(
            "Observe for filter binding",
            added = { _, e -> if (pred(e)) add(e) },
            removed = { _, e -> remove(e) }
        )
    }
}