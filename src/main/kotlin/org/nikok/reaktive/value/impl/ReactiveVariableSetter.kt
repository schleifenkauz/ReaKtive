package org.nikok.reaktive.value.impl

import org.nikok.reaktive.Observer
import org.nikok.reaktive.impl.HandlerCounter
import org.nikok.reaktive.impl.WeakReactive
import org.nikok.reaktive.value.*

internal class ReactiveVariableSetter<T>(rv: ReactiveVariable<T>, handlerCounter: HandlerCounter) : VariableSetter<T> {
    private val weak = WeakReactive(rv, handlerCounter)

    private val variable get() = weak.reactive

    override fun set(value: T): Boolean {
        variable?.set(value)
        return variable != null
    }

    override fun bind(other: ReactiveValue<T>): Observer = variable?.bind(other) ?: Observer.nothing
}