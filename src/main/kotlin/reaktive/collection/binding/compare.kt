/**
 * @author Nikolaus Knop
 */

package reaktive.collection.binding

import reaktive.collection.ReactiveCollection
import reaktive.value.binding.Binding
import reaktive.value.binding.binding
import java.util.*

fun <E : Any> ReactiveCollection<E>.minWith(comparator: Comparator<E>): Binding<E?> = binding<E?>(null) {
    val queue = PriorityQueue(now.size, comparator)
    queue.addAll(now)
    val obs = observeCollection { ch ->
        if (ch.wasRemoved) queue.remove(ch.removed)
        if (ch.wasAdded) queue.add(ch.added)
        set(queue.peek())
    }
    addObserver(obs)
}

fun <E : Any, C : Comparable<C>> ReactiveCollection<E>.minBy(selector: (E) -> C): Binding<E?> =
    minWith(Comparator.comparing(selector))

fun <E : Any> ReactiveCollection<E>.maxWith(comparator: Comparator<E>): Binding<E?> = minWith(comparator.reversed())

fun <E : Any, C : Comparable<C>> ReactiveCollection<E>.maxBy(selector: (E) -> C): Binding<E?> =
    maxWith(Comparator.comparing(selector))