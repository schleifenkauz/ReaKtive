/**
 * @author Nikolaus Knop
 */

package reaktive.list

import kotlinx.serialization.Serializable
import reaktive.collection.MutableReactiveCollection
import reaktive.impl.ReactiveListSerializer

@Serializable(ReactiveListSerializer::class)
interface MutableReactiveList<E> : ReactiveList<E>, MutableReactiveCollection<E> {
    override val now: MutableList<E>

    override val writer: ListWriter<E>
}