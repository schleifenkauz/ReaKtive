/**
 * @author Nikolaus Knop
 */

package reaktive.set

import reaktive.collection.MutableReactiveCollection

interface MutableReactiveSet<E> : ReactiveSet<E>, MutableReactiveCollection<E> {
    override val now: MutableSet<E>

    override val writer: SetWriter<E>
}