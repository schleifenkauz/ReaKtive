/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.set

interface MutableReactiveSet<E>: ReactiveSet<E> {
    override val now: MutableSet<E>
}