package reaktive.collection.binding

import reaktive.collection.ReactiveCollection
import reaktive.value.binding.*

/**
 * @return a [Binding] computing the size of this [ReactiveCollection]
 */
val ReactiveCollection<*>.size: Binding<Int>
    get() {
        return binding(now.size) {
            val obs = observeCollection { change ->
                if (change.wasRemoved) withValue { set(it - 1) }
                else if (change.wasAdded) withValue { set(it + 1) }
            }
            addObserver(obs)
        }
    }

fun ReactiveCollection<*>.isEmpty() = size.equalTo(0)

fun ReactiveCollection<*>.isNotEmpty() = size.notEqualTo(0)