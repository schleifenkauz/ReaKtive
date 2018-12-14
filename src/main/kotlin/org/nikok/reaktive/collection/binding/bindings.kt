/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.collection.binding

import org.nikok.reaktive.collection.ReactiveCollection
import org.nikok.reaktive.collection.observe
import org.nikok.reaktive.value.binding.*

fun <E> ReactiveCollection<E, *>.count(newDescription: String, pred: (E) -> Boolean): Binding<Int> {
    val matchingElements = now.filterTo(mutableSetOf(), pred)
    return binding(newDescription, matchingElements.size) {
        observe(
            "Observe for any binding $newDescription",
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

fun <E> ReactiveCollection<E, *>.any(newDescription: String, pred: (E) -> Boolean): Binding<Boolean> =
        count("help for $newDescription", pred).greaterThan(0)

fun <E> ReactiveCollection<E, *>.contains(element: E) = any("$this contains $element") { it == element }

