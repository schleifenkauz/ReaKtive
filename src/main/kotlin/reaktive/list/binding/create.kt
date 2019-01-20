/**
 * @author Nikolaus Knop
 */

package reaktive.list.binding

import reaktive.list.binding.impl.ListBindingImpl
import reaktive.list.reactive


fun <E> listBinding(
    content: Iterable<E>,
    setup: ListBindingBody<E>.() -> Unit
): ListBinding<E> =
    ListBindingImpl(content.toMutableList().reactive(), setup)