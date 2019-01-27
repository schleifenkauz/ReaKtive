/**
 *@author Nikolaus Knop
 */

package reaktive.list.impl

class IndexList(
    private val wrapped: MutableList<Int> = mutableListOf()
) {
    fun insert(element: Int): Int {
        val index = wrapped.binarySearch(element)
        val insertionPoint = if (index < 0) -(index + 1) else index
        wrapped.add(insertionPoint, element)
        return insertionPoint
    }

    fun indexOf(element: Int): Int = wrapped.binarySearch(element)

    fun remove(element: Int): Int {
        val index = indexOf(element)
        wrapped.removeAt(index)
        return index
    }

    fun incrementAllFrom(startIdx: Int) {
        for (i in startIdx + 1 until size) {
            wrapped[i]++
        }
    }

    fun decrementAllFrom(startIdx: Int) {
        for (i in startIdx..size) {
            wrapped[i]--
        }
    }

    operator fun contains(element: Int) = indexOf(element) >= 0

    operator fun get(index: Int): Int = wrapped[index]

    val size get() = wrapped.size

    companion object {
        fun <E : Comparable<E>> wrapping(sorted: MutableList<Int>) = IndexList(sorted)
    }
}