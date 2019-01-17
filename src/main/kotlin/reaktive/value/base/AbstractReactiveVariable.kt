/**
 * @author Nikolaus Knop
 */

package reaktive.value.base

import reaktive.InvalidationHandler
import reaktive.Observer
import reaktive.impl.HandlerCounter
import reaktive.impl.ObserverManager
import reaktive.value.*
import reaktive.value.binding.Binding
import reaktive.value.binding.Bindings
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

    private val bidirectionalBindings = LinkedList<Observer>()

    init {
        val handlerCounter = HandlerCounter()
        observerManager = ObserverManager(handlerCounter)
        setter = ReactiveVariableSetter(this, handlerCounter)
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

    final override fun <F> map(f: (T) -> F): Binding<F> {
        return Bindings.map(this, f)
    }

    final override fun <F> flatMap(f: (T) -> ReactiveValue<F>): Binding<F> {
        return Bindings.flatMap(this, f)
    }

    final override fun bindBidirectional(other: ReactiveVariable<T>): Observer {
        return bind(this, bidirectionalBindings, other)
    }

    private fun valueChanged(old: T) {
        val new = now
        observerManager.notifyHandlers { h -> h(this, old, new) }
    }

    private companion object {
        fun <T> bind(
            self: ReactiveVariable<T>, bidirectionalBindings: MutableList<Observer>, other: ReactiveVariable<T>
        ): Observer {
            bindBidirectionalPreconditions(self, other)
            self.set(other.get())
            val selfSetter = self.setter
            val otherSetter = other.setter
            val obs1 = other.observe { new: T -> selfSetter.set(new) }
            val obs2 = self.observe { new: T -> otherSetter.set(new) }
            val bindingObserver = obs1 and obs2
            bidirectionalBindings.add(bindingObserver)
            return bindingObserver.and { bidirectionalBindings.remove(bindingObserver) }
        }

        private fun <T> bindBidirectionalPreconditions(self: ReactiveVariable<T>, other: ReactiveVariable<T>) {
            require(other != self) { "Cannot bind $self to itself bidirectionally" }
            if (self.isBound) throw AlreadyBoundException.attemptedBindTo(self, other)
            if (other.isBound) throw AlreadyBoundException.attemptedBindTo(other, self)
        }
    }
}
