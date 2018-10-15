/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.value.fx

import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import org.nikok.reaktive.InvalidationHandler
import org.nikok.reaktive.Observer
import org.nikok.reaktive.value.ReactiveValue
import org.nikok.reaktive.value.ValueChangeHandler
import org.nikok.reaktive.value.binding.Binding
import org.nikok.reaktive.value.binding.Bindings

open class ReactiveValueAdapter<T : Any?>(private val wrapped: ObservableValue<T>) : ReactiveValue<T> {
    override fun observe(handler: ValueChangeHandler<T>): Observer {
        val listener = ChangeListener<T> { _, old, new -> handler.valueChanged(this, old, new) }
        wrapped.addListener(listener)
        return Observer(handler.description) { wrapped.removeListener(listener) }
    }

    override fun observe(handler: InvalidationHandler): Observer {
        val listener = InvalidationListener { _ -> handler.invalidated(this) }
        wrapped.addListener(listener)
        return Observer(handler.description) { wrapped.removeListener(listener) }
    }

    override fun <F> map(newDescription: String, f: (T) -> F): Binding<F> = Bindings.map(this, newDescription, f)

    override fun <F> flatMap(newDescription: String, f: (T) -> ReactiveValue<F>): Binding<F> {
        return Bindings.flatMap(this, newDescription, f)
    }

    override val description: String
        get() = "No description"

    override fun toString(): String = wrapped.toString()

    override fun get(): T = wrapped.value
}