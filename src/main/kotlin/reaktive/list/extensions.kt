/**
 * @author Nikolaus Knop
 */

package reaktive.list

import reaktive.Observer
import reaktive.list.ListChange.*
import reaktive.list.binding.listBinding
import reaktive.value.ReactiveValue
import reaktive.value.now

/**
 * Observes each element of this [ReactiveList]
 * * When an element is added it is automatically observed
 * * When an element is removed its observer is killed
 * * When the returned observer is killed the observation of all elements is stopped
 */
fun <E> ReactiveList<E>.observeEach(observe: (E) -> Observer): Observer {
    val observers = now.mapTo(mutableListOf()) { observe(it) }
    val o = observeList { ch ->
        when (ch) {
            is Added    -> observers.add(ch.index, observe(ch.added))
            is Removed  -> observers.removeAt(ch.index).kill()
            is Replaced -> {
                observers[ch.index].kill()
                observers[ch.index] = observe(ch.added)
            }
        }
    }
    return Observer {
        o.kill()
        observers.forEach { it.kill() }
        observers.clear()
    }
}

/**
 * Creates a list that holds the item if it is not null or is empty otherwise.
 */
fun <E> ReactiveValue<E?>.toList() = listBinding<E>(if (now != null) listOf(now!!) else emptyList()) {
    val o = observe { _, _, new ->
        clear()
        if (new != null) add(new)
    }
    addObserver(o)
}