/**
 *@author Nikolaus Knop
 */

package reaktive.list.binding

import reaktive.list.base.AbstractReactiveList

internal abstract class AbstractListBinding<E> : ListBinding<E>, AbstractReactiveList<E>() {
    override val now: List<E> = object : AbstractList<E>() {
        override val size: Int
            get() = this@AbstractListBinding.size

        override fun get(index: Int): E = this@AbstractListBinding.get(index)
    }

    protected abstract val size: Int

    protected abstract fun get(index: Int): E
}