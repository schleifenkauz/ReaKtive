/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.value.binding.impl

import org.nikok.kref.weak
import org.nikok.reaktive.*
import org.nikok.reaktive.Observer
import org.nikok.reaktive.value.*
import org.nikok.reaktive.value.binding.Binding
import org.nikok.reaktive.value.binding.ValueBindingBody
import java.util.*

internal class BindingImpl<T> private constructor(
    private val wrapped: ReactiveVariable<T>, body: ValueBindingBody<T>.() -> Unit
) : Binding<T>, ReactiveValue<T> by wrapped, AbstractDisposable() {
    constructor(description: String, value: T, body: ValueBindingBody<T>.() -> Unit) : this(
        reactiveVariable(
            description, value
        ), body
    )

    private val bindingBody = BindingBodyImpl(wrapped)

    init {
        bindingBody.body()
    }

    override fun doDispose() {
        bindingBody.dispose()
    }

    private class BindingBodyImpl<T>(variable: ReactiveVariable<T>) : ValueBindingBody<T>, AbstractBindingBody() {
        private val setter = variable.setter
        private val variable by weak(variable)

        override fun set(value: T) {
            setter.set(value)
        }

        override fun bind(other: ReactiveValue<T>): Observer {
            return setter.bind(other)
        }

        override fun withValue(use: (value: T) -> Unit) {
            val v = variable?.get()
            v?.let(use)
        }
    }
}