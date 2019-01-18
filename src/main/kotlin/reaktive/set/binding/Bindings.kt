package reaktive.set.binding

import reaktive.collection.ReactiveCollection
import reaktive.collection.observeCollection
import reaktive.set.ReactiveSet
import reaktive.value.binding.Binding
import reaktive.value.binding.addObservers

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

    fun <E, F> flatMap(set: ReactiveSet<E>, f: (E) -> ReactiveCollection<F>): SetBinding<F> {
        TODO("not implemented")
    }

    fun <E> subtract(set: ReactiveSet<E>, other: ReactiveCollection<E>): SetBinding<E> =
        setBinding(set.now.minus(other.now) as MutableSet<E>) {
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
}