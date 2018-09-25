package org.nikok.reaktive.collection

interface MutableReactiveCollection<E, C : CollectionChange<E>> : ReactiveCollection<E, C> {
    override val now: MutableCollection<E>
}
