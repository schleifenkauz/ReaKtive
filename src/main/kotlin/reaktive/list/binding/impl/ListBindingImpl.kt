/**
 *@author Nikolaus Knop
 */

package reaktive.list.binding.impl

import reaktive.AbstractBindingBody
import reaktive.list.*
import reaktive.list.binding.ListBinding
import reaktive.list.binding.ListBindingBody

internal class ListBindingImpl<E>(wrapped: MutableReactiveList<E>, setup: ListBindingBody<E>.() -> Unit) :
    ListBinding<E>,
    ReactiveList<E> by wrapped {
    private val body = ListBindingBodyImpl(wrapped)

    init {
        setup(body)
    }

    override fun dispose() {
        body.dispose()
    }

    private class ListBindingBodyImpl<E>(rl: MutableReactiveList<E>) : ListBindingBody<E>,
                                                                       ListWriter<E> by rl.writer,
                                                                       AbstractBindingBody()
}