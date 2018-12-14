/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.list

interface MutableReactiveList<E>: ReactiveList<E> {
    override val now: MutableList<E>
}