/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive

/**
 * Function object that maps objects of type [T] to objects of type [F]
 */
interface Mapper<T, F> : Described {
    /**
     * Maps the [value] to an object of type [F]
     */
    fun map(value: T): F
}

