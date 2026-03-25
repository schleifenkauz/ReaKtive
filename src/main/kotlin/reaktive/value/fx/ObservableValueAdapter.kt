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
            runLater { listener.changed(this, old, new) }
        }
        listenersToObservers[listener] = obs
    }

    override fun addListener(listener: InvalidationListener) {
        val obs = wrapped.observe { _, _, _ ->
            runLater { listener.invalidated(this) }
        }
        listenersToObservers[listener] = obs
    }

    override fun getValue(): T = wrapped.get()

    companion object {
        val platformCls by lazy {
            try {
                ObservableValueAdapter::class.java.classLoader.loadClass("javafx.application.Platform")
            } catch (e: ClassNotFoundException) {
                null
            } catch (e: SecurityException) {
                null
            }
        }

        private val isFxApplicationThreadMethod by lazy {
            platformCls?.getDeclaredMethod("isFxApplicationThread")
        }

        private val runLaterMethod by lazy {
            platformCls?.getDeclaredMethod("runLater", Runnable::class.java)
        }

        private fun runLater(action: Runnable) {
            when {
                isFxApplicationThreadMethod?.invoke(null) == true -> action.run()
                runLaterMethod == null -> action.run()
                else -> runLaterMethod!!.invoke(null, action)
            }
        }
    }
}