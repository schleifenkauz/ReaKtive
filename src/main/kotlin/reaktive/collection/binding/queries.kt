/**
 * @author Nikolaus Knop
 */

package reaktive.collection.binding

import reaktive.collection.ReactiveCollection
import reaktive.value.binding.Binding
import reaktive.value.binding.binding

inline fun <E> ReactiveCollection<E>.find(crossinline pred: (E) -> Boolean): Binding<E?> = binding(now.find(pred)) {
    val obs = observeCollection { ch ->
        withValue { value ->
            if (ch.wasAdded && value == null && pred(ch.element)) set(ch.element)
            else if (ch.wasRemoved && value == ch.element) set(now.find(pred))
        }
    }
    addObserver(obs)
}