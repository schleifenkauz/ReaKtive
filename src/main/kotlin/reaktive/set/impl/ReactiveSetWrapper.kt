/**
 *@author Nikolaus Knop
 */

package reaktive.set.impl

import reaktive.Observer
import reaktive.Reactive
import reaktive.and
import reaktive.collection.CollectionChange
import reaktive.set.*
import reaktive.set.base.AbstractReactiveSet

internal class ReactiveSetWrapper<out E>(
    private val wrapped: ReactiveSet<E>,
    private val extractor: (E) -> Reactive
) : AbstractReactiveSet<E>() {
    private val observer: Observer

    override val now: Set<E>
        get() = wrapped.now

    init {
        observer = wrapped.observeEach { element ->
            extractor(element).observe { fireChange(SetChange.Updated(element, this)) }
        }
    }

    override fun observeCollection(handler: (CollectionChange<E>) -> Unit): Observer =
        super.observeCollection(handler) and wrapped.observeCollection(handler)

    override fun observeSet(handler: (SetChange<E>) -> Unit): Observer =
        super.observeSet(handler) and wrapped.observeSet(handler)
}
