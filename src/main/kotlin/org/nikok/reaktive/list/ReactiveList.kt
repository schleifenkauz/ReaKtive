/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.list

import org.nikok.reaktive.collection.ReactiveCollection
import org.nikok.reaktive.collection.binding.CollectionBinding
import org.nikok.reaktive.list.binding.ListBinding

interface ReactiveList<E>: ReactiveCollection<E, ListChange<E>> {
    override val now: List<E>

    override fun <F> map(newName: String, f: (E) -> F): CollectionBinding<F, *> 

    override fun filter(newName: String, predicate: (E) -> Boolean): ListBinding<E>

    override fun plus(other: Collection<E>): ListBinding<E>

    override fun plus(other: ReactiveCollection<E, *>): ListBinding<E>

    override fun <F> flatMap(newName: String, f: (E) -> ReactiveCollection<F, *>): ListBinding<F>

    override fun minus(other: Collection<E>): ListBinding<E>

    override fun minus(other: ReactiveCollection<E, *>): ListBinding<E>
}