/**
 *@author Nikolaus Knop
 */

package reaktive.list.impl

import reaktive.Observer
import reaktive.Reactive
import reaktive.and
import reaktive.collection.CollectionChange
import reaktive.list.*
import reaktive.list.base.AbstractReactiveList

internal class ReactiveListWrapper<out E>(
    private val wrapped: ReactiveList<E>,
    private val dependencies: (E) -> Reactive
) : AbstractReactiveList<E>() {
    private val observer: Observer = wrapped.observeEach { index, element ->
        dependencies(element).observe { fireChange(ListChange.Replaced(index, wrapped, element, element)) }
    }

    override val now: List<E>
        get() = wrapped.now

    override fun observeCollection(handler: (CollectionChange<E>) -> Unit): Observer =
        super.observeCollection(handler) and wrapped.observeCollection(handler)

    override fun observeList(handler: (ListChange<E>) -> Unit): Observer =
        super.observeList(handler) and wrapped.observeList(handler)
}
