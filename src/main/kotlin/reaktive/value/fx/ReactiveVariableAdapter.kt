/**
 *@author Nikolaus Knop
 */

package reaktive.value.fx

import javafx.beans.property.Property
import reaktive.Observer
import reaktive.impl.WeakReactive
import reaktive.value.*
import reaktive.value.impl.VariableSetterImpl
import java.lang.ref.WeakReference

internal class ReactiveVariableAdapter<T>(
    private val wrapped: Property<T>,
) : ReactiveValueAdapter<T>(wrapped), ReactiveVariable<T> {
    override val setter: VariableSetter<T>
        get() = VariableSetterImpl(this)

    override fun bindBidirectional(other: ReactiveVariable<T>): Observer {
        val p = other.asProperty()
        wrapped.bindBidirectional(p)
        return BidirectionalBindObserver(WeakReference(wrapped), WeakReference(p))
    }

    override fun set(value: T) {
        wrapped.value = value
    }

    override fun bind(other: ReactiveValue<T>): Observer {
        val ov = other.asObservableValue()
        wrapped.bind(ov)
        return BindObserver(WeakReference(wrapped))
    }

    override val isBound: Boolean
        get() = wrapped.isBound

    override val weak: WeakReactive<ReactiveVariable<T>>
        get() = throw UnsupportedOperationException("Weak references to adapters are not supported")

    private class BindObserver(private val variable: WeakReference<Property<*>>) : Observer() {
        override fun doKill() {
            variable.get()?.unbind()
        }
    }

    private class BidirectionalBindObserver<T>(
        private val variable: WeakReference<Property<T>>,
        private val other: WeakReference<Property<T>>
    ) : Observer() {
        override fun doKill() {
            val other = other.get() ?: return
            variable.get()?.unbindBidirectional(other)
        }
    }
}