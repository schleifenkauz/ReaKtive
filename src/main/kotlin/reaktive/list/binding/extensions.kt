/**
 * @author Nikolaus Knop
 */

package reaktive.list.binding

import reaktive.Observer
import reaktive.list.ListChange.*
import reaktive.list.ReactiveList
import reaktive.value.ReactiveValue
import reaktive.value.now

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
                    set(idx, ch.new.now)
                    observers[idx] = observeElement(idx, ch.new)
                }
                is Removed  -> {
                    val obs = observers.removeAt(ch.index)
                    removeAt(ch.index)
                    obs.kill()
                }
                is Added    -> {
                    val obs = observeElement(ch.index, ch.element)
                    observers.add(ch.index, obs)
                    add(ch.index, ch.element.now)
                }
            }
        }
        addObserver(obs)
    }