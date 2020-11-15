/**
 *@author Nikolaus Knop
 */

package reaktive.value.binding.impl

import reaktive.*
import reaktive.value.*
import reaktive.value.binding.Binding
import reaktive.value.binding.ValueBindingBody
import java.lang.ref.WeakReference

@PublishedApi internal class BindingImpl<T>(
    private val wrapped: ReactiveVariable<T>
) : Binding<T>, ReactiveValue<T> by wrapped, AbstractDisposable() {
    private val bindingBodyImpl = BindingBodyImpl(wrapped)

    @PublishedApi internal val bindingBody: ValueBindingBody<T> get() = bindingBodyImpl

    override fun doDispose() {
        bindingBodyImpl.dispose()
    }

    private class BindingBodyImpl<T>(variable: ReactiveVariable<T>) : ValueBindingBody<T>, AbstractBindingBody() {
        private val setter = variable.setter
        private val variable by WeakReference(variable)

        override fun set(value: T) {
            setter.set(value)
        }

        override fun bind(other: ReactiveValue<T>): Observer {
            return setter.bind(other)
        }

        override fun withValue(use: (value: T) -> Unit) {
            val v = variable ?: return
            v.get().let(use)
        }
    }

    companion object {
        @PublishedApi internal inline fun <T> newBinding(initial: T, body: ValueBindingBody<T>.() -> Unit): Binding<T> =
            BindingImpl(reactiveVariable(initial)).apply { bindingBody.body() }
    }
}