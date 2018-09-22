/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive

import org.nikok.reaktive.value.ReactiveBoolean

/**
 * A function object which tests an object of type [T] and returns a [ReactiveBoolean]
 */
interface ReactivePredicate<T> : Described {
    /**
     * tests an object of type [T] and returns a [ReactiveBoolean]
     */
    fun test(t: T): ReactiveBoolean
}