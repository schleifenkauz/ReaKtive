/**
 * @author Nikolaus Knop
 */

package reaktive.list.binding

import reaktive.collection.binding.CollectionBindingBody
import reaktive.list.ListWriter

interface ListBindingBody<E> : ListWriter<E>, CollectionBindingBody<E>