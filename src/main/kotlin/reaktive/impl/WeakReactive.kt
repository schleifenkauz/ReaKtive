/**
 *@author Nikolaus Knop
 */

package reaktive.impl

import org.nikok.kref.*
import reaktive.Observer
import reaktive.Reactive
import kotlin.reflect.KProperty

internal class WeakReactive<R : Reactive>(r: R, handlerCounter: HandlerCounter) {
    private val refWrapper: RefWrapper<R>

    private val handlerCountObserver: Observer

    init {
        val ref = chooseReference(handlerCounter.hasHandlers, r)
        refWrapper = wrapper(ref)
        handlerCountObserver = handlerCounter.observeHasHandlers { hasHandlers ->
            val rct = reactive
            if (rct == null) stopObserve()
            else {
                refWrapper.ref = chooseReference(hasHandlers, rct)
            }
        }
    }

    val reactive by refWrapper

    private fun stopObserve() {
        handlerCountObserver.kill()
    }

    private fun chooseReference(hasHandlers: Boolean, r: R) = if (hasHandlers) strong(r) else weak(r)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): R? = reactive
}