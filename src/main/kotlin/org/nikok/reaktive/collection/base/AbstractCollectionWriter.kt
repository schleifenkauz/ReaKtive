package org.nikok.reaktive.collection.base

import org.nikok.reaktive.collection.CollectionWriter
import org.nikok.reaktive.impl.HandlerCounter
import org.nikok.reaktive.impl.WeakReactive
import org.nikok.reaktive.set.MutableReactiveSet

internal abstract class AbstractCollectionWriter<E>(target: MutableReactiveSet<E>, handlerCounter: HandlerCounter) :
        CollectionWriter<E> {
    private val weakTarget by WeakReactive(target, handlerCounter)
    
    final override fun add(element: E): Boolean {
        return withContent { add(element) } ?: false
    }

    final override fun remove(element: E): Boolean {
        return withContent { remove(element) } ?: false
    }

    final override fun addAll(elements: Collection<E>): Boolean {
        return withContent { addAll(elements) } ?: false
    }

    final override fun removeAll(elements: Collection<E>): Boolean {
        return withContent { removeAll(elements) } ?: false
    }

    final override fun clear() {
        withContent { clear() }
    }

    final override fun <T> withContent(block: (MutableCollection<E>) -> T): T? {
        return weakTarget?.now?.let(block)
    }
}
