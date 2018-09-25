/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.collection.base

import org.nikok.reaktive.InvalidationHandler
import org.nikok.reaktive.Observer
import org.nikok.reaktive.collection.*
import org.nikok.reaktive.impl.HandlerCounter
import org.nikok.reaktive.impl.ObserverManager

abstract class AbstractReactiveCollection<out E, C : CollectionChange<E>> : ReactiveCollection<E, C> {
    private val handlerCounter = HandlerCounter()

    private val observerManager = ObserverManager<CollectionChangeHandler<E, C>>(handlerCounter)

    final override fun observe(handler: CollectionChangeHandler<E, C>): Observer {
        return observerManager.addHandler(handler)
    }

    final override fun observe(handler: InvalidationHandler): Observer = observe(handler.asCollectionChangeHandler())

    final override fun toString(): String {
        val content = now.joinToString(prefix = "[", postfix = "]")
        return "$description: $content"
    }

    protected fun fireChange(ch: C) {
        observerManager.notifyHandlers { h -> h.handle(ch) }
    }

    private companion object {
        fun <E, Ch : CollectionChange<E>> InvalidationHandler.asCollectionChangeHandler(): CollectionChangeHandler<E, Ch> {
            return collectionChangeHandler(description) { c -> invalidated(c.modified) }
        }
    }
}