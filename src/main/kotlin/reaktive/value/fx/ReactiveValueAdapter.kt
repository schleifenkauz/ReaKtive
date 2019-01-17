/**
 *@author Nikolaus Knop
 */

package reaktive.value.fx

import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import reaktive.InvalidationHandler
import reaktive.Observer
import reaktive.value.ReactiveValue
import reaktive.value.ValueChangeHandler
import reaktive.value.binding.Binding
import reaktive.value.binding.Bindings

open class ReactiveValueAdapter<T : Any?>(private val wrapped: ObservableValue<T>) : ReactiveValue<T> {
    override fun observe(handler: ValueChangeHandler<T>): Observer {
        val listener = ChangeListener<T> { _, old, new -> handler(this, old, new) }
        wrapped.addListener(listener)
        return Observer { wrapped.removeListener(listener) }
    }

    override fun observe(handler: InvalidationHandler): Observer {
        val listener = InvalidationListener { handler(this) }
        wrapped.addListener(listener)
        return Observer { wrapped.removeListener(listener) }
    }

    override fun <F> map(f: (T) -> F): Binding<F> = Bindings.map(this, f)

    override fun <F> flatMap(f: (T) -> ReactiveValue<F>): Binding<F> {
        return Bindings.flatMap(this, f)
    }

    override fun toString(): String = wrapped.toString()

    override fun get(): T = wrapped.value
}