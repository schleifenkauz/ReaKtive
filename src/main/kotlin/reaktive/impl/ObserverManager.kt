/**
 *@author Nikolaus Knop
 */

package reaktive.impl

import reaktive.Observer
import java.util.*

internal class ObserverManager<H>(private val handlerCounter: HandlerCounter) {
    private val handlers = LinkedList<H>()
    private val afterNotifications = LinkedList<() -> Unit>()

    private var notifying = false

    fun addHandler(handler: H): Observer {
        executeSafely {
            handlers.add(handler)
            handlerCounter.newHandler()
        }
        return Observer { removeHandler(handler) }
    }

    private fun removeHandler(handler: H) {
        executeSafely {
            handlers.remove(handler)
            handlerCounter.deletedHandler()
        }
    }

    private fun executeSafely(block: () -> Unit) {
        if (notifying) afterNotifications.add(block)
        else block()
    }

    private inline fun notifying(block: () -> Unit) {
        notifying = true
        block()
        notifying = false
        afterNotifications.forEach { it -> it.invoke() }
        afterNotifications.clear()
    }

    fun notifyHandlers(notify: (H) -> Unit) {
        notifying {
            handlers.forEach(notify)
        }
    }
}