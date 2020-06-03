/**
 * @author Nikolaus Knop
 */

package reaktive.set.binding

import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.collection.observeCollection
import reaktive.set.ReactiveSet
import reaktive.value.*

/**
 * Flattens this reactive set of reactive collections, such that the result always contains all elements of all collections
 */
fun <E> ReactiveSet<ReactiveCollection<E>>.flatten() = flatMap { it }

/**
 * Returns a set binding that contains all values of the reactive values in this reactive set
 */
fun <E> ReactiveSet<ReactiveValue<E>>.values(): SetBinding<E> =
    setBinding(now.mapTo(mutableSetOf()) { it.now }) {
        val valueObservers = mutableMapOf<ReactiveValue<E>, Observer>()
        fun observe(v: ReactiveValue<E>) {
            val obs = v.observe { old, new ->
                remove(old)
                add(new)
            }
            valueObservers[v] = obs
            addObserver(obs)
        }
        for (v in now) {
            observe(v)
        }
        val obs = observeSet { ch ->
            if (ch.wasAdded) {
                add(ch.added.now)
                observe(ch.added)
            }
            if (ch.wasRemoved) {
                remove(ch.removed.now)
                valueObservers.remove(ch.removed)!!.kill()
            }
        }
        addObserver(obs)
    }

/**
 * Returns a set binding that always contains the contents of the set in this reactive value
 */
fun <E> ReactiveValue<ReactiveCollection<E>>.flattenToSet() = setBinding(now.now.toMutableSet()) {
    var setObs = now.observeCollection(
        added = { _, e -> add(e) },
        removed = { _, e -> remove(e) }
    )
    addObserver(setObs)
    val valueObs = observe { _, old, new ->
        setObs.kill()
        setObs = now.observeCollection(
            added = { _, e -> add(e) },
            removed = { _, e -> remove(e) }
        )
        addObserver(setObs)
        val removed = old.now - new.now
        removeAll(removed)
        val added = new.now - old.now
        addAll(added)
    }
    addObserver(valueObs)
}

/**
 * Returns a set binding which always contains the non-null results of applying the transformation to all elements.
 */
fun <E, F : Any> ReactiveSet<E>.mapNotNull(f: (E) -> F?): SetBinding<F> = setBinding(mutableSetOf()) {
    val map = mutableMapOf<E, Pair<F, Int>>()
    for (e in now) {
        val r = f(e) ?: continue
        map[e] = map[e]?.let { (x, c) -> x to c + 1 } ?: r to 1
        add(r)
    }
    observeCollection(
        removed = { _, e ->
            val (x, c) = map[e]!!
            if (c == 1) {
                map.remove(e)
                remove(x)
            } else map[e] = x to c - 1
        },
        added = { _, e ->
            val r = f(e) ?: return@observeCollection
            map[e] = map[e]?.let { (x, c) -> x to c + 1 } ?: r to 1
            add(r)
        }
    ).let(::addObserver)
}

/**
 * Returns a set binding only containing the non-null elements of the set.
 */
fun <E : Any> ReactiveSet<E?>.filterNotNull(): ReactiveSet<E> = setBinding(now.filterNotNullTo(mutableSetOf())) {
    observeCollection(
        added = { _, e -> if (e != null) add(e) },
        removed = { _, e -> if (e != null) remove(e) }
    )
}