package org.nikok.reaktive.value.impl

import org.nikok.kref.*
import org.nikok.reaktive.Observer
import org.nikok.reaktive.impl.HandlerCounter
import org.nikok.reaktive.value.*

internal class ReactiveVariableSetter<T>(rv: ReactiveVariable<T>, handlerCounter: HandlerCounter) : VariableSetter<T> {
    private val refWrapper: RefWrapper<ReactiveVariable<T>>

    private val handlerCountObserver: Observer

    init {
        val ref = chooseReference(handlerCounter.hasHandlers, rv)
        refWrapper = wrapper(ref)
        handlerCountObserver = handlerCounter.observeHasHandlers { hasHandlers ->
            val v = variable
            if (v == null) stopObserve()
            else {
                refWrapper.ref = chooseReference(
                    hasHandlers, v
                )
            }
        }
    }

    private fun stopObserve() {
        handlerCountObserver.kill()
    }

    private fun chooseReference(hasHandlers: Boolean, rv: ReactiveVariable<T>) =
            if (hasHandlers) strong(rv) else weak(rv)

    private val variable by refWrapper

    override fun set(value: T): Boolean {
        variable?.set(value)
        return variable != null
    }

    override fun bind(other: ReactiveValue<T>): Observer = variable?.bind(other) ?: Observer.nothing
}