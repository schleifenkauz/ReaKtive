/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive

import org.nikok.reaktive.value.ReactiveValue

/**
 * A Function object that maps an object of type [T] to an [ReactiveValue] of type [F]
 */
interface ReactiveMapper<T, F> : Described {
    /**
     * Maps an object of type [T] to an [ReactiveValue] of type [F]
     */
    fun map(value: T): ReactiveValue<F>
}