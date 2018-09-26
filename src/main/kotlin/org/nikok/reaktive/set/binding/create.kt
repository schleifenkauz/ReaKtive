/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.set.binding

import org.nikok.reaktive.set.binding.impl.SetBindingImpl

fun <E> binding(description: String, initial: MutableSet<E>, body: SetBindingBody<E>.() -> Unit): SetBinding<E> {
    return SetBindingImpl(description, initial, body)
}