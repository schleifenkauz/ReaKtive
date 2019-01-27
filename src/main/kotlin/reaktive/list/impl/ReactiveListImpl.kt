/**
 *@author Nikolaus Knop
 */

package reaktive.list.impl

import reaktive.list.ListWriter
import reaktive.list.MutableReactiveList
import reaktive.list.base.AbstractReactiveList

internal class ReactiveListImpl<E>(
    private val wrapped: MutableList<E>
) : MutableReactiveList<E>,
    AbstractReactiveList<E>() {
    override val writer: ListWriter<E> = ListWriterImpl(this, handlerCounter)

    override val now: MutableList<E> = Now()

    private inner class Now : AbstractMutableList<E>() {
        override val size: Int
            get() = wrapped.size

        override fun get(index: Int): E = wrapped[index]

        override fun add(index: Int, element: E) {
            wrapped.add(index, element)
            fireAdded(element, index)
        }

        override fun removeAt(index: Int): E {
            val e = wrapped.removeAt(index)
            fireRemoved(e, index)
            return e
        }

        override fun set(index: Int, element: E): E {
            val old = wrapped.set(index, element)
            fireReplaced(old, element, index)
            return old
        }
    }
}