package reaktive.set

import reaktive.collection.CollectionWriter

interface SetWriter<E>: CollectionWriter<E> {
    override fun <T> withContent(block: (MutableCollection<E>) -> T): T?
}
