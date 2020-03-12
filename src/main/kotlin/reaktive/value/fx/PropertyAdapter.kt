/**
 *@author Nikolaus Knop
 */

package reaktive.value.fx

import javafx.beans.property.Property
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import reaktive.Observer
import reaktive.value.ReactiveVariable

internal class PropertyAdapter<T>(private val wrapped: ReactiveVariable<T>) : ObservableValueAdapter<T>(wrapped),
                                                                              Property<T> {
    override fun setValue(value: T) {
        wrapped.set(value)
    }

    override fun getName(): String? = null

    private val binders = mutableMapOf<Property<T>, Observer>()

    override fun bindBidirectional(other: Property<T>) {
        val obs = wrapped.bindBidirectional(other.asReactiveVariable())
        binders[other] = obs
    }

    override fun unbindBidirectional(other: Property<T>?) {
        binders.remove(other)?.kill()
    }

    override fun getBean(): Any? = null

    private var binder: Observer? = null

    override fun unbind() {
        binder?.kill()
        binder = null
    }

    override fun bind(observable: ObservableValue<out T>) {
        unbind()
        binder = wrapped.bind(observable.asReactiveValue())
    }

    override fun isBound(): Boolean = binder != null
}

