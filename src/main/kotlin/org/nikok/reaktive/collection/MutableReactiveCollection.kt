package org.nikok.reaktive.collection

/**
 * A Reactive collection which can be modified
*/
interface MutableReactiveCollection<E, C : CollectionChange<E>> : ReactiveCollection<E, C> {
    override val now: MutableCollection<E>

    /**
     * @return a [CollectionWriter] which holds only a [java.lang.ref.WeakReference] to this [MutableReactiveCollection]
     * when there are no handlers observing this collection
    */
    val writer: CollectionWriter<E>
}
