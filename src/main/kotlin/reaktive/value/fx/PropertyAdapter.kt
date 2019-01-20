/**
 *@author Nikolaus Knop
 */

package reaktive.value.fx

import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import reaktive.value.ReactiveVariable

internal class PropertyAdapter<T>(private val wrapped: ReactiveVariable<T>) : ObservableValueAdapter<T>(wrapped),
                                                                              Property<T> {
    override fun setValue(value: T) {
        wrapped.set(value)
    }

    override fun getName(): String? = null

    override fun bindBidirectional(other: Property<T>) {
        wrapped.bindBidirectional(other.asReactiveVariable())
    }

    override fun getBean(): Any? {
        TODO("not implemented")
    }

    override fun unbind() {
        TODO("not implemented")
    }

    override fun bind(observable: ObservableValue<out T>?) {
        TODO("not implemented")
    }

    override fun isBound(): Boolean {
        TODO("not implemented")
    }

    override fun unbindBidirectional(other: Property<T>?) {
        TODO("not implemented")
    }
}

