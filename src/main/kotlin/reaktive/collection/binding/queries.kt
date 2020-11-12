/**
 * @author Nikolaus Knop
 */

package reaktive.collection.binding

import reaktive.collection.ReactiveCollection
import reaktive.value.binding.Binding
import reaktive.value.binding.createBinding

inline fun <E> ReactiveCollection<E>.find(crossinline pred: (E) -> Boolean): Binding<E?> = createBinding(now.find(pred)) {
    val obs = observeCollection { ch ->
        withValue { value ->
            if (ch.wasAdded && value == null && pred(ch.added)) set(ch.added)
            if (ch.wasRemoved && value == ch.removed) set(now.find(pred))
        }
    }
    addObserver(obs)
}