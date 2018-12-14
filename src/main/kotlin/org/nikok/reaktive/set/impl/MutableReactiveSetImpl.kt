/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.set.impl

import org.nikok.reaktive.set.MutableReactiveSet
import org.nikok.reaktive.set.SetWriter
import org.nikok.reaktive.set.base.AbstractReactiveSet

internal class MutableReactiveSetImpl<E>(override val description: String, private val wrapped: MutableSet<E>) :
        AbstractReactiveSet<E>(),
        MutableReactiveSet<E> {
    override val writer: SetWriter<E> = SetWriterImpl(this, handlerCounter)

    override val now: MutableSet<E> = object : Set<E> by wrapped, MutableSet<E> {
        override fun equals(other: Any?): Boolean = wrapped == other

        override fun hashCode(): Int = wrapped.hashCode()

        override fun toString(): String = wrapped.toString()

        override fun addAll(elements: Collection<E>): Boolean {
            var modified = false
            for (e in elements) if (add(e)) modified = true
            return modified
        }

        override fun clear() {
            val itr = iterator()
            while (itr.hasNext()) {
                itr.next()
                itr.remove()
            }
        }

        override fun removeAll(elements: Collection<E>): Boolean {
            var modified = false
            for (e in elements) if (remove(e)) modified = true
            return modified
        }

        override fun retainAll(elements: Collection<E>): Boolean {
            var modified = false
            val itr = iterator()
            while (itr.hasNext()) {
                if (itr.next() !in elements) {
                    itr.remove()
                    modified = true
                }
            }
            return modified
        }

        override fun add(element: E): Boolean {
            val added = wrapped.add(element)
            if (added) {
                fireAdded(element)
            }
            return added
        }

        override fun remove(element: E): Boolean {
            val removed = wrapped.remove(element)
            if (removed) {
                fireRemoved(element)
            }
            return removed
        }

        override fun iterator(): MutableIterator<E> {
            return Itr()
        }

        private inner class Itr : MutableIterator<E> {
            private val itr = wrapped.iterator()
            private var curr: E? = null

            override fun hasNext(): Boolean = itr.hasNext()

            override fun next(): E {
                val next = itr.next()
                curr = next
                return next
            }

            override fun remove() {
                val c = curr
                if (c == null) throw IllegalStateException()
                else {
                    itr.remove()
                    fireRemoved(c)
                }
            }
        }

        override val size: Int
            get() = wrapped.size

    }
}