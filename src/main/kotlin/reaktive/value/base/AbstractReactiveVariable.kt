/**
 * @author Nikolaus Knop
 */

package reaktive.value.base

import reaktive.InvalidationHandler
import reaktive.Observer
import reaktive.impl.*
import reaktive.value.*
import reaktive.value.impl.ReactiveVariableSetter
import java.util.*

/**
 * Abstract base class for [ReactiveVariable]s implementing
 * * Observing
 * * binding uni and bidirectionally
 * * map and flatMap
 * @constructor return a new [AbstractReactiveVariable]
 */
abstract class AbstractReactiveVariable<T> : ReactiveVariable<T>, AbstractVariable<T>() {
    private val observerManager: ObserverManager<ValueChangeHandler<T>>

    final override val setter: VariableSetter<T>

    final override val weak: WeakReactive<ReactiveVariable<T>>

    private val bidirectionalBindings = LinkedList<Observer>()

    init {
        val handlerCounter = HandlerCounter()
        observerManager = ObserverManager(handlerCounter)
        weak = WeakReactive(this, handlerCounter)
        setter = ReactiveVariableSetter(weak)
    }

    final override fun set(value: T) {
        if (value != now) {
            val old = get()
            super.set(value)
            valueChanged(old)
        }
    }

    final override fun observe(handler: ValueChangeHandler<T>): Observer {
        return observerManager.addHandler(handler)
    }

    final override fun observe(handler: InvalidationHandler): Observer {
        val changeHandler: ValueChangeHandler<T> = { rv, _, _ -> handler(rv) }
        return observerManager.addHandler(changeHandler)
    }

    final override fun bindBidirectional(other: ReactiveVariable<T>): Observer {
        require(other != this) { "Cannot bind $this to itself bidirectionally" }
        if (this.isBound) throw AlreadyBoundException.attemptedBindTo(this, other)
        if (other.isBound) throw AlreadyBoundException.attemptedBindTo(other, this)
        this.set(other.get())
        val selfSetter = this.setter
        val otherSetter = other.setter
        val obs1 = other.observe { new: T -> selfSetter.set(new) }
        val obs2 = this.observe { new: T -> otherSetter.set(new) }
        val bindingObserver = obs1 and obs2
        bidirectionalBindings.add(bindingObserver)
        return bindingObserver.and { bidirectionalBindings.remove(bindingObserver) }
    }

    private fun valueChanged(old: T) {
        val new = now
        observerManager.notifyHandlers { h -> h(this, old, new) }
    }
}
