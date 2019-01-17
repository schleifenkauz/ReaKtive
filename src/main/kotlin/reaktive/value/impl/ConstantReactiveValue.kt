/**
 *@author Nikolaus Knop
 */

package reaktive.value.impl

import reaktive.InvalidationHandler
import reaktive.Observer
import reaktive.value.*
import reaktive.value.base.AbstractValue
import reaktive.value.binding.*

internal class ConstantReactiveValue<T>(private val value: T) : ReactiveValue<T>,
                                                                AbstractValue<T>() {
    override fun observe(handler: ValueChangeHandler<T>): Observer = Observer.nothing

    override fun observe(handler: InvalidationHandler) = Observer.nothing

    override fun <F> map(f: (T) -> F): Binding<F> {
        return binding(f(now))
    }

    override fun <F> flatMap(f: (T) -> ReactiveValue<F>): Binding<F> {
        return f(now).asBinding()
    }

    override fun get(): T = value
}