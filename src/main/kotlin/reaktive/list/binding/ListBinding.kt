/**
 * @author Nikolaus Knop
 */

package reaktive.list.binding

import reaktive.collection.binding.CollectionBinding
import reaktive.list.ReactiveList

interface ListBinding<out E> : CollectionBinding<E>, ReactiveList<E>