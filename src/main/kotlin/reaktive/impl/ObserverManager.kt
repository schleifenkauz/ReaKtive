/**
 *@author Nikolaus Knop
 */

package reaktive.impl

import reaktive.Observer
import reaktive.ObserverImpl
import java.lang.ref.WeakReference
import java.util.*

internal class ObserverManager<H>(private val handlerCounter: HandlerCounter) {
    private val handlers = LinkedList<WeakReference<H>>()
    private val afterNotifications = LinkedList<() -> Unit>()

    private var notifying = false

    fun addHandler(handler: H): Observer {
        val ref = WeakReference(handler)
        executeSafely {
            handlers.add(ref)
            handlerCounter.newHandler()
        }
        return ObserverImpl(this, handler)
    }

    internal fun removeHandler(ref: H) {
        executeSafely {
            handlers.removeIf { it.get() === ref }
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
        afterNotifications.forEach { it.invoke() }
        afterNotifications.clear()
    }

    fun notifyHandlers(notify: (H) -> Unit) {
        notifying {
            val itr = handlers.listIterator()
            for (ref in itr) {
                val handler = ref.get()
                if (handler == null) {
                    itr.remove()
                    continue
                }
                try {
                    notify(handler)
                } catch (ex: Throwable) {
                    System.err.println("Exception in handler: ")
                    ex.printStackTrace()
                }
            }
        }
    }
}