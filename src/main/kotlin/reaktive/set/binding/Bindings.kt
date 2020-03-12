package reaktive.set.binding

import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.collection.observeCollection
import reaktive.set.ReactiveSet
import reaktive.value.binding.Binding
import reaktive.value.binding.addObservers

internal object Bindings {
    fun <E, F> map(set: ReactiveSet<E>, f: (E) -> F): SetBinding<F> =
        setBinding(set.now.mapTo(mutableSetOf(), f)) {
            set.observeCollection(
                removed = { _, e -> remove(f(e)) },
                added = { _, e -> add(f(e)) }
            ).let(::addObserver)
        }

    fun <E> filter(set: ReactiveSet<E>, pred: (E) -> Boolean): SetBinding<E> =
        setBinding(set.now.filterTo(mutableSetOf(), pred)) {
            set.observeCollection(
                added = { _, e -> if (pred(e)) add(e) },
                removed = { _, e -> remove(e) }
            ).let(::addObserver)
        }

    fun <E, F> flatMap(set: ReactiveSet<E>, f: (E) -> ReactiveCollection<F>): SetBinding<F> =
        setBinding(mutableSetOf()) {
            val partObservers = mutableMapOf<ReactiveCollection<F>, Observer>()
            val parts = mutableMapOf<E, ReactiveCollection<F>>()
            fun addedElement(element: E) {
                val part = f(element)
                addAll(part.now)
                parts[element] = part
                val obs = part.observeCollection { ch ->
                    if (ch.wasAdded) add(ch.element)
                    else if (ch.wasRemoved) remove(ch.element)
                }
                addObserver(obs)
                partObservers[part] = obs
            }

            fun removedElement(element: E) {
                val part = parts.remove(element)!!
                val obs = partObservers.remove(part)!!
                obs.kill()
                for (removed in part.now) {
                    if (parts.values.none { removed in it.now }) remove(removed)
                }
            }
            for (e in set.now) addedElement(e)
            val obs = set.observeSet { ch ->
                if (ch.wasAdded) addedElement(ch.element)
                else if (ch.wasRemoved) removedElement(ch.element)
            }
            addObserver(obs)
        }

    fun <E> subtract(set: ReactiveSet<E>, other: ReactiveCollection<E>): SetBinding<E> =
        setBinding(set.now.minus(other.now).toMutableSet()) {
            val obs1 = set.observeSet { ch ->
                if (ch.wasAdded && ch.element !in other.now) add(ch.element)
                else if (ch.wasRemoved) remove(ch.element)
            }
            val obs2 = other.observeCollection { ch ->
                if (ch.wasAdded) remove(ch.element)
                if (ch.wasRemoved && ch.element in set.now) add(ch.element)
            }
            addObservers(obs1, obs2)
        }

    fun <E> union(
        collections: Iterable<ReactiveCollection<E>>
    ): SetBinding<E> {
        val initial = collections.flatMapTo(mutableSetOf()) { it.now }
        return setBinding(initial) {
            for (col in collections) {
                val obs = col.observeCollection { ch ->
                    if (ch.wasRemoved && collections.none { ch.element in it.now }) remove(ch.element)
                    else if (ch.wasAdded) add(ch.element)
                }
                addObserver(obs)
            }
        }
    }

    fun <E, T> fold(set: ReactiveSet<E>, op: (T, E) -> T): Binding<T> {
            TODO("not implemented")
    }

    fun <E> intersect(set1: ReactiveSet<E>, set2: ReactiveSet<E>): SetBinding<E> =
        setBinding(set1.now.intersect(set2.now).toMutableSet()) {
            val obs1 = set1.observeSet { ch ->
                if (ch.wasRemoved) remove(ch.element)
                else if (ch.wasAdded && ch.element in set2.now) add(ch.element)
            }
            val obs2 = set2.observeSet { ch ->
                if (ch.wasRemoved) remove(ch.element)
                else if (ch.wasAdded && ch.element in set1.now) add(ch.element)
            }
            addObservers(obs1, obs2)
        }
}