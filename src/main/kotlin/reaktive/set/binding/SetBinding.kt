/**
 * @author Nikolaus Knop
 */

package reaktive.set.binding

import reaktive.collection.binding.CollectionBinding
import reaktive.set.ReactiveSet

interface SetBinding<out E> : CollectionBinding<E>, ReactiveSet<E>