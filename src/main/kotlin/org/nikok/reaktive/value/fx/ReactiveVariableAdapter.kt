/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.value.fx

import javafx.beans.property.Property
import org.nikok.reaktive.Observer
import org.nikok.reaktive.value.*
import org.nikok.reaktive.value.impl.VariableSetterImpl

internal class ReactiveVariableAdapter<T>(private val wrapped: Property<T>) : ReactiveValueAdapter<T>(wrapped),
                                                                              ReactiveVariable<T> {
    override val setter: VariableSetter<T>
        get() = VariableSetterImpl(this)

    override fun bindBidirectional(other: ReactiveVariable<T>): Observer {
        val p = other.asProperty()
        wrapped.bindBidirectional(p)
        return Observer("Bind bidirectional $this to $other") { wrapped.unbindBidirectional(p) }
    }

    override fun set(value: T) {
        wrapped.value = value
    }

    override fun bind(other: ReactiveValue<T>): Observer {
        val ov = other.asObservableValue()
        wrapped.bind(ov)
        return Observer("Bind $this to $other") { wrapped.unbind() }
    }

    override val isBound: Boolean
        get() = wrapped.isBound

}