package org.nikok.reaktive.set.binding

import org.nikok.reaktive.collection.observe
import org.nikok.reaktive.set.ReactiveSet
import org.nikok.reaktive.value.binding.Binding
import org.nikok.reaktive.value.binding.binding

internal object Bindings {
    fun <E, F> map(set: ReactiveSet<E>, newDescription: String, f: (E) -> F): SetBinding<F> =
            setBinding(newDescription, set.now.mapTo(mutableSetOf(), f)) {
                set.observe(
                    "Observe for map binding",
                    removed = { _, e -> add(f(e)) },
                    added = { _, e -> remove(f(e)) }
                ).let(::addObserver)
            }

    fun <E> filter(set: ReactiveSet<E>, newDescription: String, pred: (E) -> Boolean): SetBinding<E> =
            setBinding(newDescription, set.now.filterTo(mutableSetOf(), pred)) {
                set.observe(
                    "Observe for filter binding",
                    added = { _, e -> if (pred(e)) add(e) },
                    removed = { _, e -> remove(e) }
                ).let(::addObserver)
            }

    fun <E, F> fold(set: ReactiveSet<E>, newDescription: String, initial: F, op: (F, E) -> F): Binding<F> =
            binding(newDescription, set.now.fold(initial, op)) {
                set.observe(
                    "Observer for fold binding $newDescription",
                    added = { _, e -> withValue { v ->
                        set(op(v, e))
                    } },
                    removed = { _, e -> withValue { v ->
                        TODO("Don't know what to do when element is removed")
                    } }
                ).let(::addObserver)
            }
}