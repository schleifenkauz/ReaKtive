/**
 *@author Nikolaus Knop
 */

package reaktive.list.impl

import reaktive.collection.base.AbstractCollectionWriter
import reaktive.impl.HandlerCounter
import reaktive.list.ListWriter
import reaktive.list.MutableReactiveList

internal class ListWriterImpl<E>(
    target: MutableReactiveList<E>,
    handlerCounter: HandlerCounter
) : ListWriter<E>, AbstractCollectionWriter<E>(target, handlerCounter) {
    override fun <T> withList(block: (MutableList<E>) -> T): T? {
        return withContent { block(it as MutableList<E>) }
    }

    override fun add(idx: Int, element: E) {
        withList { it.add(idx, element) }
    }

    override fun set(idx: Int, element: E) {
        withList { it.set(idx, element) }
    }

    override fun removeAt(idx: Int) {
        withList { it.removeAt(idx) }
    }
}