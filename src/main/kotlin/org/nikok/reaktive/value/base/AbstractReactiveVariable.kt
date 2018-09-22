/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value.base

import org.nikok.reaktive.*
import org.nikok.reaktive.Observer
import org.nikok.reaktive.impl.HandlerCounter
import org.nikok.reaktive.impl.ObserverManager
import org.nikok.reaktive.value.*
import org.nikok.reaktive.value.binding.Binding
import org.nikok.reaktive.value.binding.binding
import org.nikok.reaktive.value.impl.ReactiveVariableSetter
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
        val changeHandler = handler.asValueChangeHandler<T>()
        return observerManager.addHandler(changeHandler)
    }

    final override fun <F> map(newDescription: String, f: (T) -> F): Binding<F> {
        return binding<F>(newDescription, dependencies(this)) { f(now) }
    }

    final override fun <F> flatMap(newDescription: String, f: (T) -> ReactiveValue<F>): Binding<F> {
        val first = f(now)
        return binding(newDescription, first.now) {
            var oldBindObserver: Observer? = bind(first)
            val obs = observe("observer for binding $newDescription") { _, _, new ->
                oldBindObserver?.kill()
                oldBindObserver = bind(f(new))
            }
            addObserver(obs)
        }
    }

    final override fun bindBidirectional(other: ReactiveVariable<T>): Observer {
        return bind(this, bidirectionalBindings, other)
    }

    private fun valueChanged(old: T) {
        val new = now
        observerManager.notifyHandlers { h -> h.valueChanged(this, old, new) }
    }

    private companion object {
        fun <T> InvalidationHandler.asValueChangeHandler(): ValueChangeHandler<T> {
            return valueChangeHandler<T, Unit>(description) { r, _, _ -> invalidated(r) }
        }

        fun <T> bind(
            self: ReactiveVariable<T>, bidirectionalBindings: MutableList<Observer>, other: ReactiveVariable<T>
        ): Observer {
            bindBidirectionalPreconditions(self, other)
            self.set(other.get())
            val selfSetter = self.setter
            val otherSetter = other.setter
            val description = "bind $self and $other bidirectionally"
            val obs1 = other.observe(description) { new: T -> selfSetter.set(new) }
            val obs2 = self.observe(description) { new: T -> otherSetter.set(new) }
            val bindingObserver = obs1 and obs2
            bidirectionalBindings.add(bindingObserver)
            return bindingObserver.and("remove $bindingObserver") { bidirectionalBindings.remove(bindingObserver) }
        }

        private fun <T> bindBidirectionalPreconditions(self: ReactiveVariable<T>, other: ReactiveVariable<T>) {
            require(other != self) { "Cannot bind $self to itself bidirectionally" }
            if (self.isBound) throw AlreadyBoundException.attemptedBindTo(self, other)
            if (other.isBound) throw AlreadyBoundException.attemptedBindTo(other, self)
        }
    }
}
