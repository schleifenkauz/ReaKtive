/**
 * @author Nikolaus Knop
 */

package reaktive.collection.binding

import reaktive.collection.ReactiveCollection
import reaktive.collection.observeCollection
import reaktive.value.binding.*

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

fun <E> ReactiveCollection<E>.any(pred: (E) -> Boolean): Binding<Boolean> =
    count(pred).greaterThan(0)

fun <E> ReactiveCollection<E>.contains(element: E) = any { it == element }

