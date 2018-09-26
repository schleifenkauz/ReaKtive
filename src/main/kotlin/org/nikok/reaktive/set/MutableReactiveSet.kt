/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.set

import org.nikok.reaktive.collection.MutableReactiveCollection

interface MutableReactiveSet<E>: ReactiveSet<E>, MutableReactiveCollection<E, SetChange<E>>{
    override val now: MutableSet<E>

    override val writer: SetWriter<E>
}