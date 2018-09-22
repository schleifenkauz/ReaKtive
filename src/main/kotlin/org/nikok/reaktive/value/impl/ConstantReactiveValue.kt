/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.value.impl

import org.nikok.reaktive.InvalidationHandler
import org.nikok.reaktive.Observer
import org.nikok.reaktive.value.*
import org.nikok.reaktive.value.base.AbstractValue
import org.nikok.reaktive.value.binding.*

internal class ConstantReactiveValue<T>(override val description: String, private val value: T) : ReactiveValue<T>,
                                                                                                  AbstractValue<T>() {
    override fun observe(handler: ValueChangeHandler<T>): Observer = Observer.nothing

    override fun observe(handler: InvalidationHandler) = Observer.nothing

    override fun <F> map(newDescription: String, f: (T) -> F): Binding<F> {
        return binding(newDescription, f(now))
    }

    override fun <F> flatMap(newDescription: String, f: (T) -> ReactiveValue<F>): Binding<F> {
        return f(now).asBinding(newDescription)
    }

    override fun get(): T = value
}