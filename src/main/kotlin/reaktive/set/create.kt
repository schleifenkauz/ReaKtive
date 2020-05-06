/**
 * @author Nikolaus Knop
 */

package reaktive.set

import reaktive.collection.CollectionChange
import reaktive.collection.ReactiveCollection
import reaktive.set.binding.*
import reaktive.set.impl.*

/**
 * @return a [ReactiveSet] containing [elements]
 */
fun <E> reactiveSet(vararg elements: E): MutableReactiveSet<E> =
    MutableReactiveSetImpl(elements.toMutableSet())

/**
 * @return a [ReactiveSet] consisting of the elements in the receiver
 */
fun <E> Iterable<E>.toReactiveSet(): MutableReactiveSet<E> =
    MutableReactiveSetImpl(toMutableSet())

/**
 * @return a [MutableReactiveSet] wrapping the [MutableSet]
 */
fun <E> MutableSet<E>.reactive(): MutableReactiveSet<E> = MutableReactiveSetImpl(this)

/**
 * @return an unmodifiable set containing the specified [elements]
 */
fun <E> unmodifiableReactiveSet(elements: Iterable<E>): ReactiveSet<E> =
    UnmodifiableReactiveSet(elements)

/**
 * @return an unmodifiable set containing no elements
 */
fun <E> emptyReactiveSet(): ReactiveSet<E> = EmptyReactiveSet

/**
 * @return a reactive set holding all the elements of this reactive collection, changes are synchronized
 */
fun <E> ReactiveCollection<E>.asSet(): SetBinding<E> = if (this is ReactiveSet<E>) this.asBinding() else
    setBinding(now.toMutableSet()) {
        val handler = { ch: CollectionChange<E> ->
            if (ch.wasAdded) add(ch.added)
            if (ch.wasRemoved && ch.removed !in now) remove(ch.removed)
        }
        addObserver(observeCollection(handler))
    }