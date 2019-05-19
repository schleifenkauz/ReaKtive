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
                add(ch.element.now)
                observe(ch.element)
            } else if (ch.wasRemoved) {
                remove(ch.element.now)
                valueObservers.remove(ch.element)!!.kill()
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