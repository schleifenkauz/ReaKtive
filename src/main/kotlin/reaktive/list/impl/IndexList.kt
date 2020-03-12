/**
 *@author Nikolaus Knop
 */

package reaktive.list.impl

internal class IndexList(
    private val wrapped: MutableList<Int> = mutableListOf()
) {
    fun insert(element: Int): Int {
        val index = indexFor(element)
        wrapped.add(index, element)
        return index
    }

    fun indexOf(element: Int): Int = wrapped.binarySearch(element)

    fun indexFor(element: Int): Int = correctBinarySearchIndex(indexOf(element))

    fun remove(element: Int): Int {
        val index = indexOf(element)
        if (index >= 0) {
            wrapped.removeAt(index)
            return index
        }
        return -1
    }

    fun incrementAllFrom(startIdx: Int) {
        for (i in startIdx until size) {
            wrapped[i]++
        }
    }

    fun decrementAllFrom(startIdx: Int) {
        for (i in startIdx until size) {
            wrapped[i]--
        }
    }

    operator fun contains(element: Int) = indexOf(element) >= 0

    operator fun get(index: Int): Int = wrapped[index]

    val size get() = wrapped.size

    companion object {
        fun <E : Comparable<E>> wrapping(sorted: MutableList<Int>) = IndexList(sorted)

        fun correctBinarySearchIndex(inverted: Int) = if (inverted > 0) inverted else -(inverted + 1)
    }
}