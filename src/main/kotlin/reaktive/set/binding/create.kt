/**
 * @author Nikolaus Knop
 */

package reaktive.set.binding

import reaktive.set.binding.impl.SetBindingImpl

fun <E> setBinding(
    initial: MutableSet<E>,
    body: SetBindingBody<E>.() -> Unit
): SetBinding<E> {
    return SetBindingImpl(initial, body)
}