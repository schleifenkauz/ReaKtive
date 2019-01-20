package reaktive.impl

import reaktive.Observer
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
        return Observer { handlers.remove(onHasListeners) }
    }
}
