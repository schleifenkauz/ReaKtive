/**
 * @author Nikolaus Knop
 */

package reaktive.collection.binding

import reaktive.collection.ReactiveCollection
import reaktive.value.ReactiveValue
import reaktive.value.binding.Binding
import reaktive.value.binding.binding

fun <E> ReactiveCollection<E>.minWith(comparator: Comparator<E>): Binding<E?> = binding(now.minWith(comparator)) {
    val obs = observeCollection { ch ->
        if (ch.wasRemoved)
            withValue { v -> if (ch.element == v) set(now.minWith(comparator)) }
        else if (ch.wasAdded) withValue { v ->
            if (v == null || comparator.compare(ch.element, v) > 0) set(ch.element)
        }
    }
    addObserver(obs)
}

fun <E, C : Comparable<C>> ReactiveCollection<E>.minBy(selector: (E) -> C): Binding<E?> =
    minWith(Comparator.comparing(selector))

inline fun <E, C : Comparable<C>> ReactiveCollection<E>.minByR(selector: (E) -> ReactiveValue<C>): Binding<E?> =
    binding<E?>(null) {
        TODO()
    }