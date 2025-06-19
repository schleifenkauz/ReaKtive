/**
 *@author Nikolaus Knop
 */

package reaktive.value.fx

import javafx.beans.InvalidationListener
import javafx.beans.WeakInvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.beans.value.WeakChangeListener
import reaktive.InvalidationHandler
import reaktive.Observer
import reaktive.impl.WeakReactive
import reaktive.value.ReactiveValue
import reaktive.value.ValueChangeHandler
import java.lang.ref.WeakReference

open class ReactiveValueAdapter<T : Any?>(private val wrapped: ObservableValue<T>) : ReactiveValue<T> {
    override fun observe(handler: ValueChangeHandler<T>): Observer {
        val listener = ChangeListener { _, old, new -> handler(this, old, new) }
        wrapped.addListener(WeakChangeListener(listener))
        return ChangeObserver(wrapped, listener)
    }

    override fun observe(handler: InvalidationHandler): Observer {
        val listener = WeakInvalidationListener { handler(this) }
        wrapped.addListener(WeakInvalidationListener(listener))
        return InvalidationObserver(wrapped, listener)
    }

    override fun toString(): String = wrapped.toString()

    override fun get(): T = wrapped.value

    override val weak: WeakReactive<ReactiveValue<T>>
        get() = throw UnsupportedOperationException("Weak references to adapters are not supported")

    private class ChangeObserver<T>(
        value: ObservableValue<T>,
        private val listener: ChangeListener<T>,
    ) : Observer() {
        private val value = WeakReference(value)

        override fun doKill() {
            value.get()?.removeListener(listener)
        }
    }

    private class InvalidationObserver(
        value: ObservableValue<*>,
        private val listener: InvalidationListener,
    ) : Observer() {
        private val value = WeakReference(value)

        override fun doKill() {
            value.get()?.removeListener(listener)
        }
    }
}