/**
 *@author Nikolaus Knop
 */

package reaktive.value.binding.impl

import org.nikok.kref.weak
import reaktive.*
import reaktive.value.*
import reaktive.value.binding.Binding
import reaktive.value.binding.ValueBindingBody

internal class BindingImpl<T> private constructor(
    private val wrapped: ReactiveVariable<T>, body: ValueBindingBody<T>.() -> Unit
) : Binding<T>, ReactiveValue<T> by wrapped, AbstractDisposable() {
    constructor(value: T, body: ValueBindingBody<T>.() -> Unit) : this(
        reactiveVariable(
            value
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