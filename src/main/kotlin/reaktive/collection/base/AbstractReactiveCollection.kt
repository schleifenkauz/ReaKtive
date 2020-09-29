/**
 *@author Nikolaus Knop
 */

package reaktive.collection.base

import reaktive.InvalidationHandler
import reaktive.Observer
import reaktive.collection.CollectionChange
import reaktive.collection.ReactiveCollection
import reaktive.impl.HandlerCounter
import reaktive.impl.ObserverManager

/**
 * Skeletal implementation of [ReactiveCollection]
 * @constructor
 */
internal abstract class AbstractReactiveCollection<out E, out C : CollectionChange<E>> : ReactiveCollection<E> {
    protected val handlerCounter = HandlerCounter()

    private val observerManager = ObserverManager<(C) -> Unit>(handlerCounter)

    override fun observeCollection(handler: (CollectionChange<E>) -> Unit): Observer = implObserve(handler)

    protected fun implObserve(handler: (C) -> Unit) = observerManager.addHandler(handler)

    final override fun observe(handler: InvalidationHandler): Observer =
        observeCollection { ch -> handler(ch.modified) }

    final override fun toString(): String = "${javaClass.name}(now=$now)"

    /**
     * Notifies all handlers observing this [ReactiveCollection] that a change [ch] has happened
     */
    protected fun fireChange(ch: @UnsafeVariance C) {
        observerManager.notifyHandlers { handler -> handler(ch) }
    }
}