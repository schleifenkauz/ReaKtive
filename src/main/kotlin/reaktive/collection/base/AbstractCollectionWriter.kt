package reaktive.collection.base

import reaktive.collection.CollectionWriter
import reaktive.collection.MutableReactiveCollection
import reaktive.impl.HandlerCounter
import reaktive.impl.WeakReactive

internal abstract class AbstractCollectionWriter<E>(
    target: MutableReactiveCollection<E>,
    handlerCounter: HandlerCounter
) :
    CollectionWriter<E> {
    private val weakTarget by WeakReactive(target, handlerCounter)

    final override fun add(element: E): Boolean {
        return withContent { it.add(element) } ?: false
    }

    final override fun remove(element: Any?): Boolean {
        return withContent { it.remove(element) } ?: false
    }

    final override fun addAll(elements: Collection<E>): Boolean {
        return withContent { it.addAll(elements) } ?: false
    }

    final override fun removeAll(elements: Collection<E>): Boolean {
        return withContent { it.removeAll(elements) } ?: false
    }

    final override fun clear() {
        withContent { it.clear() }
    }

    final override fun <T> withContent(block: (MutableCollection<E>) -> T): T? {
        return weakTarget?.now?.let(block)
    }
}
