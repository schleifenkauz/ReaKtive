package reaktive.impl

import reaktive.Observer
import reaktive.ObserverImpl
import java.lang.ref.WeakReference
import java.util.*
import kotlin.properties.Delegates.observable

internal class HandlerCounter {
    val hasHandlers get() = handlerCount > 0

    private var handlerCount by observable(0) { _, _, _ -> notifyHandlers() }

    fun newHandler() {
        ++handlerCount
    }

    fun deletedHandler() {
        --handlerCount
    }

    private val handlers = LinkedList<(Boolean) -> Unit>()

    private fun notifyHandlers() {
        val hasListeners = handlerCount > 0
        for (h in handlers) h.invoke(hasListeners)
    }

    fun observeHasHandlers(onHasListeners: (Boolean) -> Unit): Observer {
        handlers.add(onHasListeners)
        return HandlerCountObserver(WeakReference(this), onHasListeners)
    }

    private class HandlerCountObserver(
        private val handlerCounter: WeakReference<HandlerCounter>,
        private val onHasHandlers: (Boolean) -> Unit,
    ) : Observer() {
        override fun doKill() {
            handlerCounter.get()?.handlers?.remove(onHasHandlers)
        }
    }
}
