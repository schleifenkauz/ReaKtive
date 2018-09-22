/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value

import org.nikok.reaktive.Described

/**
 * A value of type [T]
 */
interface Value<out T> : Described {
    /**
     * @return the value
     */
    fun get(): T

    /**
     * @return a String containing the description and value of this [Value]
     */
    override fun toString(): String
}