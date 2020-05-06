/**
 * @author Nikolaus Knop
 */

package reaktive.collection.binding

import reaktive.collection.ReactiveCollection
import reaktive.value.ReactiveValue
import reaktive.value.binding.Binding
import reaktive.value.binding.binding
import java.util.*

fun <E : Any> ReactiveCollection<E>.minWith(comparator: Comparator<E>): Binding<E?> = binding<E?>(null) {
    val queue = PriorityQueue(now.size, comparator)
    queue.addAll(now)
    val obs = observeCollection { ch ->
        if (ch.wasRemoved) {
            queue.remove(ch.removed)
            withValue { v -> if (ch.removed == v) set(queue.peek()) }
        }
        if (ch.wasAdded) withValue { v ->
            queue.add(ch.added)
            if (v == null || comparator.compare(ch.added, v) < 0) set(ch.added)
        }
    }
    addObserver(obs)
}

fun <E : Any, C : Comparable<C>> ReactiveCollection<E>.minBy(selector: (E) -> C): Binding<E?> =
    minWith(Comparator.comparing(selector))

inline fun <E, C : Comparable<C>> ReactiveCollection<E>.minByR(selector: (E) -> ReactiveValue<C>): Binding<E?> =
    binding<E?>(null) {
        TODO()
    }