/**
 *@author Nikolaus Knop
 */

package reaktive.impl

import reaktive.Observer
import reaktive.Reactive
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

class WeakReactive<out R : Reactive> internal constructor(reactive: R, private val handlerCounter: HandlerCounter) {
    private var strong: R? = reactive
    private var weak: WeakReference<R>? = null

    private val handlerCountObserver: Observer

    init {
        chooseReference()
        handlerCountObserver = handlerCounter.observeHasHandlers {
            if (get() == null) stopObserve()
            else chooseReference()
        }
    }

    fun get() = strong ?: weak?.get()

    private fun stopObserve() {
        handlerCountObserver.kill()
    }

    private fun chooseReference() {
        if (handlerCounter.hasHandlers && weak != null) {
            strong = weak!!.get()
            weak = null
        } else {
            weak = WeakReference(strong)
            strong = null
        }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): R? = get()
}