/**
 * @author Nikolaus Knop
 */

package reaktive.list

import reaktive.collection.MutableReactiveCollection

interface MutableReactiveList<E> : ReactiveList<E>, MutableReactiveCollection<E> {
    override val now: MutableList<E>

    override val writer: ListWriter<E>
}