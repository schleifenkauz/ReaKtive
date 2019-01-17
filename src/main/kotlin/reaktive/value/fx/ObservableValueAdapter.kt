/**
 *@author Nikolaus Knop
 */

package reaktive.value.fx

import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import reaktive.Observer
import reaktive.value.ReactiveValue

internal open class ObservableValueAdapter<T>(private val wrapped: ReactiveValue<T>) : ObservableValue<T> {
    private val listenersToObservers = mutableMapOf<Any, Observer>()

    override fun removeListener(listener: ChangeListener<in T>) {
        listenersToObservers[listener]?.kill()
    }

    override fun removeListener(listener: InvalidationListener) {
        listenersToObservers[listener]?.kill()
    }

    override fun addListener(listener: ChangeListener<in T>) {
        val obs = wrapped.observe { _, old, new ->
            listener.changed(this, old, new)
        }
        listenersToObservers[listener] = obs
    }

    override fun addListener(listener: InvalidationListener) {
        val obs = wrapped.observe { _, _, _ -> listener.invalidated(this) }
        listenersToObservers[listener] = obs
    }

    override fun getValue(): T = wrapped.get()
}