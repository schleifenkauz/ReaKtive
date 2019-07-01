package reaktive.value.impl

import reaktive.Observer
import reaktive.impl.WeakReactive
import reaktive.value.*

internal class ReactiveVariableSetter<T>(
    private val weak: WeakReactive<ReactiveVariable<T>>
) : VariableSetter<T> {

    private val variable get() = weak.reactive

    override fun set(value: T): Boolean {
        variable?.set(value)
        return variable != null
    }

    override fun bind(other: ReactiveValue<T>): Observer = variable?.bind(other) ?: Observer.nothing
}