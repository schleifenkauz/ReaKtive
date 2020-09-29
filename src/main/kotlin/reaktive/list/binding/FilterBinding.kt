/**
 *@author Nikolaus Knop
 */

package reaktive.list.binding

import reaktive.Observer
import reaktive.list.ListChange.*
import reaktive.list.ReactiveList
import reaktive.list.impl.IndexList

internal class FilterBinding<E>(
    private val source: ReactiveList<E>,
    private val predicate: (E) -> Boolean
) : AbstractListBinding<E>() {
    private val contained = source.now.mapTo(mutableListOf()) { predicate(it) }

    private val indices = IndexList.wrapping<Int>(source.now.indices.filterTo(mutableListOf()) { idx ->
        contained[idx]
    })

    private val observer: Observer = startObserving(source)

    override val size: Int
        get() = indices.size

    override fun get(index: Int): E = source.now[indices[index]]

    private fun startObserving(source: ReactiveList<E>): Observer = source.observeList { ch ->
        when (ch) {
            is Replaced -> {
                val oldSatisfies = contained[ch.index]
                val newSatisfies = predicate(ch.added)
                contained[ch.index] = newSatisfies
                if (oldSatisfies && newSatisfies) {
                    fireReplaced(ch.removed, ch.added, indices.indexOf(ch.index))
                } else if (oldSatisfies) {
                    val idx = indices.remove(ch.index)
                    fireRemoved(ch.removed, idx)
                } else if (newSatisfies) {
                    val idx = indices.insert(ch.index)
                    fireAdded(ch.added, idx)
                }
            }
            is Removed -> {
                contained.removeAt(ch.index)
                val idx = indices.remove(ch.index)
                if (idx >= 0) {
                    indices.decrementAllFrom(idx)
                    fireRemoved(ch.removed, idx)
                } else {
                    indices.decrementAllFrom(-(idx + 1))
                }
            }
            is Added -> {
                val satisfies = predicate(ch.added)
                contained.add(ch.index, satisfies)
                if (satisfies) {
                    val transformedIndex = indices.insert(ch.index)
                    indices.incrementAllFrom(transformedIndex + 1)
                    fireAdded(ch.added, transformedIndex)
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