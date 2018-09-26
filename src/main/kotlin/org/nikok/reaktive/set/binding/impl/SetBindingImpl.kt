/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.set.binding.impl

import org.nikok.reaktive.AbstractBindingBody
import org.nikok.reaktive.set.*
import org.nikok.reaktive.set.binding.SetBinding
import org.nikok.reaktive.set.binding.SetBindingBody

internal class SetBindingImpl<E> private constructor(
    private val wrapped: MutableReactiveSet<E>,
    setup: (SetBindingBody<E>) -> Unit
) : SetBinding<E>, ReactiveSet<E> by wrapped {
    constructor(description: String, content: MutableSet<E>, setup: (SetBindingBody<E>) -> Unit):
            this(content.toReactiveSet(description), setup)

    private val body = SetBindingBodyImpl(wrapped)

    init {
        setup(body)
    }

    override fun dispose() {
        body.dispose()
    }

    private class SetBindingBodyImpl<E>(rs: MutableReactiveSet<E>) : SetBindingBody<E>,
                                                                     SetWriter<E> by rs.writer,
                                                                     AbstractBindingBody()
}