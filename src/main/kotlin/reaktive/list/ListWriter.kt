/**
 * @author Nikolaus Knop
 */

package reaktive.list

import reaktive.collection.CollectionWriter

interface ListWriter<E> : CollectionWriter<E> {
    fun <T> withList(block: (MutableList<E>) -> T): T?

    fun add(idx: Int, element: E)

    fun set(idx: Int, element: E)

    fun removeAt(idx: Int)
}