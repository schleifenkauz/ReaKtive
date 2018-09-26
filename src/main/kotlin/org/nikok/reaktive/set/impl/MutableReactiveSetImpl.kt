/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.set.impl

import org.nikok.reaktive.set.MutableReactiveSet
import org.nikok.reaktive.set.SetWriter
import org.nikok.reaktive.set.base.AbstractReactiveSet
import java.lang.IllegalStateException

internal class MutableReactiveSetImpl<E>(override val description: String, private val wrapped: MutableSet<E>) :
        AbstractReactiveSet<E>(),
        MutableReactiveSet<E> {
    override val writer: SetWriter<E> = SetWriterImpl(this, handlerCounter)

    override val now: MutableSet<E> = object : AbstractMutableSet<E>() {
        override fun add(element: E): Boolean {
            val added = wrapped.remove(element)
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
                else remove(c)
            }
        }

        override val size: Int
            get() = wrapped.size

    }
}