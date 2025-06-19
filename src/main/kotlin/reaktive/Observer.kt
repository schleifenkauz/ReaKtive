/**
 *@author Nikolaus Knop
 */

package reaktive

import reaktive.impl.ObserverManager
import java.lang.ref.WeakReference

abstract class Observer {
    private var isKilled = false

    fun kill() {
        check(!isKilled) { "Cannot kill $this when already killed" }
        doKill()
        isKilled = true
    }

    protected abstract fun doKill()

    fun tryKill() {
        if (!isKilled) doKill()
    }

    companion object {
        /**
         * An [Observer] that does nothing on being killed
         */
        val nothing: Observer get() = ObserverImpl<Nothing?>(null, null)
    }
}

internal class CompositeObserver(private val observers: Iterable<Observer>) : Observer() {
    override fun doKill() {
        observers.forEach(Observer::kill)
    }
}

/**
 * Result of observing a [Reactive]
 */
internal class ObserverImpl<H>(
    private val manager: WeakReference<ObserverManager<H>>?,
    private val handler: H?,
) : Observer() {
    constructor(manager: ObserverManager<H>, handler: H) : this(WeakReference(manager), handler)

    private var killed = false

    /**
     * Assuming `o` is a [Reactive] and `observer`
     * was returned by `o.observe {  }` than o.kill() stops observing `o`
     * @throws IllegalStateException when called twice
     */
    override fun doKill() {
        if (manager != null && handler != null) {
            manager.get()?.removeHandler(handler)
        }
        killed = true
    }
}