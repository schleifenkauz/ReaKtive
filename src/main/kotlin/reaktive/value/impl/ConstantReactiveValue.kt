/**
 *@author Nikolaus Knop
 */

package reaktive.value.impl

import reaktive.InvalidationHandler
import reaktive.Observer
import reaktive.impl.HandlerCounter
import reaktive.impl.WeakReactive
import reaktive.value.ReactiveValue
import reaktive.value.ValueChangeHandler
import reaktive.value.base.AbstractValue

internal class ConstantReactiveValue<T>(private val value: T) : ReactiveValue<T>,
                                                                AbstractValue<T>() {
    override fun observe(handler: ValueChangeHandler<T>): Observer = Observer.nothing

    override fun observe(handler: InvalidationHandler) = Observer.nothing

    override fun get(): T = value

    override val weak: WeakReactive<ReactiveValue<T>> = WeakReactive(this, HandlerCounter())
}