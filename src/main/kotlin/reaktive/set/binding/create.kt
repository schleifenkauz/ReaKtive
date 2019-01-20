/**
 * @author Nikolaus Knop
 */

package reaktive.set.binding

import reaktive.set.ReactiveSet
import reaktive.set.binding.impl.SetBindingImpl
import reaktive.set.impl.UnmodifiableReactiveSet

fun <E> setBinding(
    initial: MutableSet<E>,
    body: SetBindingBody<E>.() -> Unit
): SetBinding<E> {
    return SetBindingImpl(initial, body)
}

fun <E> ReactiveSet<E>.asBinding(): SetBinding<E> =
    if (this is SetBinding) this
    else object : SetBinding<E>, ReactiveSet<E> by this {
        override fun dispose() {}
    }

fun <E> unmodifiableSetBinding(
    elements: Iterable<E>
): SetBinding<E> = UnmodifiableReactiveSet(elements).asBinding()