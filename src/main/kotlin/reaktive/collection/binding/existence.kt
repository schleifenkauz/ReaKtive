/**
 * @author Nikolaus Knop
 */

package reaktive.collection.binding

import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.collection.observeCollection
import reaktive.list.unmodifiableReactiveList
import reaktive.value.*
import reaktive.value.binding.*


/**
 * @return a [ReactiveBoolean] which holds `true` only
 * when all elements of this [ReactiveCollection] fulfill the given [predicate]
 */
inline fun <E> ReactiveCollection<E>.allR(crossinline predicate: (E) -> ReactiveBoolean): Binding<Boolean> =
    countR(predicate).equalTo(size)

inline fun <E> ReactiveCollection<E>.all(crossinline predicate: (E) -> Boolean) = allR { reactiveValue(predicate(it)) }

@PublishedApi internal inline fun <E> ValueBindingBody<Int>.observeElement(
    el: E,
    crossinline predicate: (E) -> ReactiveBoolean,
    fulfilling: MutableSet<E>
): Observer {
    val pred = predicate(el)
    if (pred.now) {
        fulfilling.add(el)
    }
    val obs = pred.observe { _, _, fulfills ->
        if (fulfills) fulfilling.add(el) else fulfilling.remove(el)
        set(fulfilling.size)
    }
    addObserver(obs)
    return obs
}

inline fun <E> ReactiveCollection<E>.countR(crossinline predicate: (E) -> ReactiveBoolean): Binding<Int> =
    binding(0) {
        val fulfilling = mutableSetOf<E>()
        val map = now.associateTo(mutableMapOf()) { it to observeElement(it, predicate, fulfilling) }
        set(fulfilling.size)
        val obs = this@countR.observeCollection(
            added = { _, element ->
                val obs = observeElement(element, predicate, fulfilling)
                map[element] = obs
                set(fulfilling.size)
            },
            removed = { _, element ->
                val obs = map[element]
                obs?.kill()
                fulfilling.remove(element)
                set(fulfilling.size)
            })
        addObserver(obs)
    }

/**
 * @return an integer binding containing the number of elements in this collection that fulfill the given predicate
 */
inline fun <E> ReactiveCollection<E>.count(crossinline pred: (E) -> Boolean): Binding<Int> {
    val matchingElements = now.filterTo(mutableSetOf(), pred)
    return binding(matchingElements.size) {
        observeCollection(
            added = { _, e ->
                if (pred(e)) {
                    matchingElements.add(e)
                    withValue { set(it + 1) }
                }
            },
            removed = { _, e ->
                if (e in matchingElements) withValue { set(it + 1) }
            }
        )
    }
}

/**
 *
 */
inline fun <E> ReactiveCollection<E>.anyR(crossinline predicate: (E) -> ReactiveBoolean): Binding<Boolean> =
    countR(predicate).notEqualTo(reactiveValue(0))

/**
 * @return a boolean binding which holds `true` only if
 * any of the elements in this collection fulfills the given predicate
 */
fun <E> ReactiveCollection<E>.any(pred: (E) -> Boolean): Binding<Boolean> =
    count(pred).greaterThan(0)

/**
 * @return A [ReactiveBoolean] which holds `true` only when [element] is contained in this collection
 */
fun <E> ReactiveCollection<E>.contains(element: E): ReactiveBoolean = contains(reactiveValue(element))

/**
 * @return a [ReactiveBoolean]
 */
fun <E> ReactiveCollection<E>.contains(element: ReactiveValue<E>) = binding(element.now in this.now) {
    val collectionObserver = observeCollection { ch ->
        if (ch.wasAdded && ch.element == element.now) set(true)
        else if (ch.wasRemoved && ch.element == element.now) set(false)
    }
    val valueObserver = element.observe { e: E -> set(e in now) }
    addObservers(collectionObserver, valueObserver)
}

/**
 * @return A [ReactiveBoolean] which holds `true` only when all [elements] are contained in this collection
 */
fun <E> ReactiveCollection<E>.containsAll(elements: Collection<@UnsafeVariance E>): Binding<Boolean> =
    containsAll(unmodifiableReactiveList(elements))

/**
 * @return A [ReactiveBoolean] which holds `true` only when all [elements] are contained in this collection
 */
fun <E> ReactiveCollection<E>.containsAll(elements: ReactiveCollection<@UnsafeVariance E>) =
    elements.allR { this.contains(it) }
