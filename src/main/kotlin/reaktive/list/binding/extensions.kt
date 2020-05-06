/**
 * @author Nikolaus Knop
 */

package reaktive.list.binding

import javafx.collections.ObservableList
import reaktive.Observer
import reaktive.collection.binding.size
import reaktive.list.ListChange.*
import reaktive.list.ReactiveList
import reaktive.value.*
import reaktive.value.binding.map

/**
 * Returns a list binding that always holds the values of the reactive values in the list
*/
fun <E> ReactiveList<ReactiveValue<E>>.values(): ListBinding<E> =
    listBinding(now.map { it.get() }) {
        fun observeElement(idx: Int, el: ReactiveValue<E>): Observer {
            val obs = el.observe { _, _, new ->
                set(idx, new)
            }
            addObserver(obs)
            return obs
        }

        val observers = now.mapIndexedTo(mutableListOf()) { idx, it ->
            observeElement(idx, it)
        }
        val obs = observeCollection { ch ->
            when (ch) {
                is Replaced -> {
                    val idx = ch.index
                    val old = observers[idx]
                    old.kill()
                    set(idx, ch.added.now)
                    observers[idx] = observeElement(idx, ch.added)
                }
                is Removed  -> {
                    val obs = observers.removeAt(ch.index)
                    removeAt(ch.index)
                    obs.kill()
                }
                is Added    -> {
                    val obs = observeElement(ch.index, ch.added)
                    observers.add(ch.index, obs)
                    add(ch.index, ch.added.now)
                }
            }
        }
        addObserver(obs)
    }

fun <E> ReactiveValue<ReactiveList<E>>.flatten(): ListBinding<E> = listBinding(now.now) {
    var obs: Observer? = null
    val o = forEach { l ->
        if (obs != null) {
            obs!!.kill()
            clear()
            addAll(l.now)
        }
        obs = l.observeList { ch ->
            when (ch) {
                is Removed  -> removeAt(ch.index)
                is Added    -> add(ch.index, ch.added)
                is Replaced -> set(ch.index, ch.added)
            }
        }
        addObserver(obs!!)
    }
    addObserver(o)
}

private fun <E> ObservableList<E>.bind(other: ReactiveList<E>): Observer {
    setAll(other.now)
    return other.observeList { ch ->
        when (ch) {
            is Removed  -> removeAt(ch.index)
            is Added    -> add(ch.index, ch.added)
            is Replaced -> set(ch.index, ch.added)
        }
    }
}


/**
 * l.get(idx) == l.get(reactiveValue(i)) for any l: ReactiveList<E>, any type E and any i: Int
*/
operator fun <E> ReactiveList<E>.get(index: Int) = get(reactiveValue(index))

/**
 * Return a binding holding the last item of the list or `null` if the list is empty
*/
fun <E> ReactiveList<E>.last() = get(size.map { it - 1 })

/**
 * Return a binding holding the first item of the list or `null` if the list is empty
*/
fun <E> ReactiveList<E>.first() = get(0)