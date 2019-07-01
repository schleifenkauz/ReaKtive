/**
 *@author Nikolaus Knop
 */

package reaktive.value.fx

import javafx.beans.property.Property
import reaktive.Observer
import reaktive.impl.WeakReactive
import reaktive.value.*
import reaktive.value.impl.VariableSetterImpl

internal class ReactiveVariableAdapter<T>(private val wrapped: Property<T>) : ReactiveValueAdapter<T>(wrapped),
                                                                              ReactiveVariable<T> {
    override val setter: VariableSetter<T>
        get() = VariableSetterImpl(this)

    override fun bindBidirectional(other: ReactiveVariable<T>): Observer {
        val p = other.asProperty()
        wrapped.bindBidirectional(p)
        return Observer { wrapped.unbindBidirectional(p) }
    }

    override fun set(value: T) {
        wrapped.value = value
    }

    override fun bind(other: ReactiveValue<T>): Observer {
        val ov = other.asObservableValue()
        wrapped.bind(ov)
        return Observer { wrapped.unbind() }
    }

    override val isBound: Boolean
        get() = wrapped.isBound

    override val weak: WeakReactive<ReactiveVariable<T>>
        get() = throw UnsupportedOperationException("Weak references to adapters are not supported")

}