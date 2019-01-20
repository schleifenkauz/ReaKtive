/**
 * @author Nikolaus Knop
 */

package reaktive.set.binding

import reaktive.Observer
import reaktive.collection.ReactiveCollection
import reaktive.set.ReactiveSet
import reaktive.value.*

fun <E> ReactiveSet<ReactiveCollection<E>>.flatten() = flatMap { it }

fun <E> ReactiveSet<ReactiveValue<E>>.values(): SetBinding<E> =
    setBinding(now.mapTo(mutableSetOf()) { it.now }) {
        val valueObservers = mutableMapOf<ReactiveValue<E>, Observer>()
        fun observe(v: ReactiveValue<E>) {
            val obs = v.observe { old, new ->
                remove(old)
                add(new)
            }
            valueObservers[v] = obs
            addObserver(obs)
        }
        for (v in now) {
            observe(v)
        }
        val obs = observeSet { ch ->
            if (ch.wasAdded) {
                add(ch.element.now)
                observe(ch.element)
            } else if (ch.wasRemoved) {
                remove(ch.element.now)
                valueObservers.remove(ch.element)!!.kill()
            }
        }
        addObserver(obs)
    }