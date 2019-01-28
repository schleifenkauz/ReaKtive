/**
 *@author Nikolaus Knop
 */

package reaktive.list.binding

import reaktive.Observer
import reaktive.list.ListChange.*
import reaktive.list.ReactiveList
import reaktive.list.base.AbstractReactiveList
import reaktive.list.impl.IndexList

internal class FilterBinding<E>(
    private val source: AbstractReactiveList<E>,
    private val predicate: (E) -> Boolean
) : AbstractListBinding<E>() {
    private val indices = IndexList.wrapping<Int>(source.now.indices.filterTo(mutableListOf()) { idx ->
        predicate(source.now[idx])
    })

    private val observer: Observer = startObserving(source)

    override val size: Int
        get() = indices.size

    override fun get(index: Int): E = source.now[indices[index]]

    private fun startObserving(source: ReactiveList<E>): Observer = source.observeList { ch ->
        when (ch) {
            is Replaced -> {
                val oldSatisfies = ch.index in indices
                val newSatisfies = predicate(ch.new)
                if (oldSatisfies && newSatisfies) {
                    fireReplaced(ch.old, ch.new, indices.indexOf(ch.index))
                } else if (oldSatisfies) {
                    val idx = indices.remove(ch.index)
                    fireRemoved(ch.old, idx)
                } else if (newSatisfies) {
                    val idx = indices.insert(ch.index)
                    fireAdded(ch.new, idx)
                }
            }
            is Removed  -> {
                val idx = indices.remove(ch.index)
                if (idx >= 0) {
                    indices.decrementAllFrom(idx)
                    fireRemoved(ch.element, idx)
                } else {
                    indices.decrementAllFrom(-(idx + 1))
                }
            }
            is Added    -> {
                if (predicate(ch.element)) {
                    val transformedIndex = indices.insert(ch.index)
                    indices.incrementAllFrom(transformedIndex + 1)
                    fireAdded(ch.element, transformedIndex)
                } else {
                    indices.incrementAllFrom(indices.indexFor(ch.index))
                }
            }
        }
    }

    override fun dispose() {
        observer.kill()
    }
}