/**
 * @author Nikolaus Knop
 */

package reaktive.set

import reaktive.Observer

/**
 * Observes each element of this [ReactiveSet]
 * * When an element is added it is automatically observed
 * * When an element is removed its observer is killed
 * * When the returned observer is killed the observation of all elements is stopped
 */
fun <E> ReactiveSet<E>.observeEach(observe: (E) -> Observer): Observer {
    val observers = mutableMapOf<E, Observer>()
    for (e in now) observers[e] = observe(e)
    val o = observeSet { ch ->
        if (ch.wasAdded) observers[ch.element] = observe(ch.element)
        else if (ch.wasRemoved) observers.remove(ch.element)!!.kill()
    }
    return Observer {
        o.kill()
        observers.values.forEach { it.kill() }
        observers.clear()
    }
}
