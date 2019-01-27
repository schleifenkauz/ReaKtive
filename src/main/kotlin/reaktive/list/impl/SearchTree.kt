/**
 *@author Nikolaus Knop
 */

package reaktive.list.impl

import reaktive.list.impl.SearchTree.Node.*
import reaktive.list.impl.SearchTree.Node.Empty.size
import java.util.*
import kotlin.Comparator

internal class SearchTree<E> private constructor(
    private val comparator: Comparator<E>,
    elements: Iterable<E>
) {
    private sealed class Node<out E> {
        abstract val size: Int

        abstract val element: E

        data class Branch<E>(
            override var size: Int,
            override var element: E,
            var lower: Node<E>,
            var higher: Node<E>
        ) :
            Node<E>()

        data class Leaf<E>(override var element: E) : Node<E>() {
            override val size: Int
                get() = 1
        }

        object Empty : Node<Nothing>() {
            override val size: Int
                get() = 0

            override val element: Nothing
                get() = throw NoSuchElementException("Empty node has no element")
        }
    }

    private var root: Node<E>

    init {
        val sorted = elements.sortedWith(comparator)
        root = constructRootFromSortedList(sorted)
    }

    fun get(index: Int): E = boundsCheck(index) { getNode(index, root).element }

    private tailrec fun <E> getNode(index: Int, node: Node<E>): Node<E> =
        when (node) {
            is Empty  -> throw AssertionError()
            is Leaf   -> if (index > 0) throw AssertionError() else node
            is Branch ->
                when {
                    index == node.lower.size -> node
                    index > node.lower.size  -> getNode(index - node.lower.size - 1, node.higher)
                    else                     -> getNode(index, node.lower)
                }
        }

    private inline fun <R> boundsCheck(index: Int, action: () -> R): R =
        if (index >= size || index < 0) throw IndexOutOfBoundsException("Index: $index, size: $size")
        else action()

    private operator fun E.compareTo(other: E) = comparator.compare(this, other)

    fun add(element: E): Boolean {
        val r = root
        when (r) {
            is Empty  -> root = Leaf(element)
            is Leaf   ->
                root = constructBranch(element, r.element)
            is Branch -> add(r, element)
        }
        return true
    }

    private fun constructBranch(el1: E, el2: E) =
        if (el1 > el2) Branch(2, el1, Leaf(el2), Empty)
        else Branch(2, el2, Leaf(el1), Empty)

    private tailrec fun add(node: Branch<E>, element: E) {
        val comp = element.compareTo(node.element)
        node.size++
        when {
            comp > 0 && node.higher is Leaf  -> node.higher = constructBranch(element, node.higher.element)
            comp > 0 && node.higher is Empty -> node.higher = Leaf(element)
            comp > 0                         -> add(node.higher as Branch, element)
            comp < 0 && node.lower is Leaf   -> node.lower = constructBranch(element, node.lower.element)
            comp < 0 && node.lower is Empty  -> node.lower = Leaf(element)
            comp < 0                         -> add(node.lower as Branch, element)
            comp == 0                        -> TODO()
        }
    }

    fun addAll(elements: Collection<E>): Boolean {
        for (element in elements) {
            add(element)
        }
        return true
    }

    fun clear() {
        root = Empty
    }

    fun remove(element: E): Boolean {
        val r = root
        return when (r) {
            is Empty  -> false
            is Leaf   -> if (element.compareTo(r.element) == 0) {
                root = Empty
                true
            } else false
            is Branch -> remove(element, r)
        }
    }

    private tailrec fun remove(element: E, node: Branch<E>): Boolean = remove(element, node)


    fun removeAll(elements: Collection<E>): Boolean {
        var removed = false
        for (element in elements) {
            if (remove(element)) removed = false
        }
        return removed
    }

    fun retainAll(elements: Collection<E>): Boolean {
        val itr = iterator()
        var removed = false
        while (itr.hasNext()) {
            if (itr.next() !in elements) {
                itr.remove()
                removed = true
            }
        }
        return removed
    }

    fun contains(element: E): Boolean = indexOf(element) != -1

    fun containsAll(elements: Collection<E>): Boolean = elements.all { this.contains(it) }

    fun indexOf(element: E): Int = indexOf(element, root, 0)

    @Suppress("NON_TAIL_RECURSIVE_CALL")
    private tailrec fun indexOf(element: E, node: Node<E>, acc: Int): Int = when (node) {
        is Empty  -> -1
        is Leaf   -> if (element == node.element) acc else -1
        is Branch -> {
            val comp = comparator.compare(element, node.element)
            when {
                comp > 0 -> indexOf(element, node.higher, acc + node.lower.size + 1)
                comp < 0 -> indexOf(element, node.lower, acc)
                else     ->
                    indexOf(element, node.lower, acc).takeIf { it != -1 }
                        ?: indexOf(element, node.higher, acc + node.lower.size + 1)
            }
        }
    }

    fun lastIndexOf(element: E): Int = lastIndexOf(element, root, 0)

    private tailrec fun lastIndexOf(element: E, node: Node<E>, acc: Int): Int = when (node) {
        is Empty  -> -1
        is Leaf   -> if (element == node.element) acc else -1
        is Branch -> {
            val comp = comparator.compare(element, node.element)
            when {
                comp > 0 -> lastIndexOf(element, node.higher, acc + node.lower.size + 1)
                comp < 0 -> lastIndexOf(element, node.lower, acc)
                else     ->
                    indexOf(element, node.higher, acc + node.lower.size + 1).takeIf { it != -1 }
                        ?: indexOf(element, node.lower, acc)
            }
        }
    }

    fun listIterator(): ListIterator<E> = listIterator(0)

    fun listIterator(index: Int): ListIterator<E> = ListItr(root)

    private class ListItr<E>(private var node: Node<E>) : ListIterator<E> {
        override fun hasNext(): Boolean {
            TODO("not implemented")
        }

        override fun hasPrevious(): Boolean {
            TODO("not implemented")
        }

        override fun next(): E {
            TODO("not implemented")
        }

        override fun nextIndex(): Int {
            TODO("not implemented")
        }

        override fun previous(): E {
            TODO("not implemented")
        }

        override fun previousIndex(): Int {
            TODO("not implemented")
        }
    }

    fun iterator(): MutableIterator<E> = MutableItr(root)

    private class MutableItr<E>(root: Node<E>) : MutableIterator<E> {
        private val stack = LinkedList<Node<E>>()

        private val next = stack.firstOrNull()

        init {
            initializeStack(root, stack)
        }

        override fun hasNext(): Boolean = next != null

        override fun next(): E {
            TODO("not implemented")
        }

        override fun remove() {
            TODO("not implemented")
        }

        companion object {
            private tailrec fun <E> initializeStack(node: Node<E>, acc: LinkedList<Node<E>>) {
                when (node) {
                    is Empty  -> {
                    }
                    is Leaf   -> acc.push(node)
                    is Branch -> {
                        acc.push(node)
                        initializeStack(node.lower, acc)
                    }
                }
            }
        }
    }

    fun subList(fromIndex: Int, toIndex: Int): List<E> {
        TODO("not implemented")
    }

    companion object {
        fun <E> of(comparator: Comparator<E>, elements: Iterable<E>): SearchTree<E> =
            SearchTree(comparator, elements)

        fun <E : Comparable<E>> of(elements: Iterable<E>): SearchTree<E> =
            of(Comparator.naturalOrder(), elements)

        fun <E> empty(comparator: Comparator<E>): SearchTree<E> = of(comparator, emptyList())

        fun <E : Comparable<E>> empty(): SearchTree<E> = of(emptyList())

        fun <E> fromSortedList(list: List<E>): SearchTree<E> = TODO()

        private fun <E> constructRootFromSortedList(elements: List<E>): Node<E> = when {
            elements.isEmpty() -> Empty
            elements.size == 1 -> Leaf(elements.first())
            else               -> {
                val midIndex = elements.size / 2
                val element = elements[midIndex]
                val lower = constructRootFromSortedList(elements.take(midIndex))
                val higher = constructRootFromSortedList(elements.drop(midIndex + 1))
                Branch(elements.size, element, lower, higher)
            }
        }
    }
}
