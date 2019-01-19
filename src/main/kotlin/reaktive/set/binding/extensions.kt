/**
 * @author Nikolaus Knop
 */

package reaktive.set.binding

import reaktive.collection.ReactiveCollection
import reaktive.set.ReactiveSet

fun <E> ReactiveSet<ReactiveCollection<E>>.flatten() = flatMap { it }

