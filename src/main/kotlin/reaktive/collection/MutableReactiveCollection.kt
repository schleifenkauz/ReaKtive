package reaktive.collection

/**
 * A Reactive collection which can be modified
*/
interface MutableReactiveCollection<E> : ReactiveCollection<E> {
    override val now: MutableCollection<E>

    /**
     * @return a [CollectionWriter] which holds only a [java.lang.ref.WeakReference] to this [MutableReactiveCollection]
     * when there are no handlers observing this collection
    */
    val writer: CollectionWriter<E>
}
