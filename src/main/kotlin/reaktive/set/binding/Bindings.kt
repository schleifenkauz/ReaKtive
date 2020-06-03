package reaktive.set.binding

import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.collection.observeCollection
import reaktive.set.ReactiveSet
import reaktive.value.binding.Binding
import reaktive.value.binding.addObservers

internal object Bindings {
    fun <E, F> map(set: ReactiveSet<E>, f: (E) -> F): SetBinding<F> = setBinding(mutableSetOf()) {
        val map = mutableMapOf<E, Pair<F, Int>>()
        for (e in set.now) {
            val r = f(e)
            map[e] = map[e]?.let { (x, c) -> x to c + 1 } ?: r to 1
            add(r)
        }
        set.observeCollection(
            removed = { _, e ->
                val (x, c) = map[e]!!
                if (c == 1) {
                    map.remove(e)
                    remove(x)
                } else map[e] = x to c - 1
            },
            added = { _, e ->
                val r = f(e)
                map[e] = map[e]?.let { (x, c) -> x to c + 1 } ?: r to 1
                add(r)
            }
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
                    if (ch.wasAdded) add(ch.added)
                    if (ch.wasRemoved) remove(ch.removed)
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
                if (ch.wasAdded) addedElement(ch.added)
                if (ch.wasRemoved) removedElement(ch.removed)
            }
            addObserver(obs)
        }

    @Suppress("UNCHECKED_CAST")
    fun <E> subtract(set: ReactiveSet<E>, other: ReactiveCollection<Any?>): SetBinding<E> =
        setBinding(set.now.minus(other.now as Iterable<E>).toMutableSet()) {
            val obs1 = set.observeSet { ch ->
                if (ch.wasAdded && ch.added !in other.now) add(ch.added)
                if (ch.wasRemoved) remove(ch.removed)
            }
            val obs2 = other.observeCollection { ch ->
                if (ch.wasAdded) remove(ch.added)
                if (ch.wasRemoved && ch.removed in set.now) add(ch.removed as E)
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
                    if (ch.wasRemoved && collections.none { ch.removed in it.now }) remove(ch.removed)
                    if (ch.wasAdded) add(ch.added)
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
                if (ch.wasRemoved) remove(ch.removed)
                if (ch.wasAdded && ch.added in set2.now) add(ch.added)
            }
            val obs2 = set2.observeSet { ch ->
                if (ch.wasRemoved) remove(ch.removed)
                if (ch.wasAdded && ch.added in set1.now) add(ch.added)
            }
            addObservers(obs1, obs2)
        }
}