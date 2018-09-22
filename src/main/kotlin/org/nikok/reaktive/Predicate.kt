/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive

/**
 * A predicate that tests objects of type [T]
 */
interface Predicate<T> : Described {
    /**
     * Tests [test] and returns `true` if [test] matches this [Predicate] `false` otherwise
     */
    fun test(test: T): Boolean
}