/**
 *@author Nikolaus Knop
 */

package reaktive.set.binding.impl

import reaktive.AbstractBindingBody
import reaktive.set.*
import reaktive.set.binding.SetBinding
import reaktive.set.binding.SetBindingBody

internal class SetBindingImpl<E> private constructor(
    private val wrapped: MutableReactiveSet<E>,
    setup: (SetBindingBody<E>) -> Unit
) : SetBinding<E>, ReactiveSet<E> by wrapped {
    constructor(
        content: MutableSet<E>,
        setup: (SetBindingBody<E>) -> Unit
    ) :
            this(content.toReactiveSet(), setup)

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