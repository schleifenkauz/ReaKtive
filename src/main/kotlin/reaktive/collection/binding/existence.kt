/**
 * @author Nikolaus Knop
 */

package reaktive.collection.binding

import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.collection.observeCollection
import reaktive.list.unmodifiableReactiveList
import reaktive.value.ReactiveBoolean
import reaktive.value.binding.*
import reaktive.value.now


/**
 * @return A [ReactiveBoolean] which holds `true` only when all [elements] are contained in this collection
 */
fun <E> ReactiveCollection<E>.containsAll(elements: ReactiveCollection<@UnsafeVariance E>) =
    elements.all { this.contains(it) }

/**
 * @return A [ReactiveBoolean] which holds `true` only when all [elements] are contained in this collection
 */
fun <E> ReactiveCollection<E>.containsAll(elements: Collection<@UnsafeVariance E>): Binding<Boolean> =
    containsAll(unmodifiableReactiveList(elements))

/**
 * @return a [ReactiveBoolean] which holds `true` only
 * when all elements of this [ReactiveCollection] fulfill the given [predicate]
 */
@JvmName("allReactive")
fun <E> ReactiveCollection<E>.all(predicate: (E) -> ReactiveBoolean): Binding<Boolean> =
    countReactive(predicate).equalTo(size)

@JvmName("countReactive")
fun <E> ReactiveCollection<E>.countReactive(predicate: (E) -> ReactiveBoolean): Binding<Int> =
    binding(0) {
        val fulfilling = mutableSetOf<E>()
        fun observeElement(el: E): Observer {
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

        val map = now.associateTo(mutableMapOf()) { it to observeElement(it) }
        set(fulfilling.size)
        val obs = this@countReactive.observeCollection(
            added = { _, element ->
                val obs = observeElement(element)
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
fun <E> ReactiveCollection<E>.count(pred: (E) -> Boolean): Binding<Int> {
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
 * @return a boolean binding which holds `true` only if
 * any of the elements in this collection fulfills the given predicate
 */
fun <E> ReactiveCollection<E>.any(pred: (E) -> Boolean): Binding<Boolean> =
    count(pred).greaterThan(0)

/**
 *
 */
@JvmName("anyReactive")
fun <E> ReactiveCollection<E>.any(predicate: (E) -> ReactiveBoolean): Binding<Boolean> =

/**
 * @return A [ReactiveBoolean] which holds `true` only when [element] is contained in this collection
 */
fun <E> ReactiveCollection<E>.contains(element: E): ReactiveBoolean = any { it == element }