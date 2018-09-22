/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive

/**
 * An Object that describes itself
 * * Useful for debugging
 */
interface Described {
    /**
     * @return the description of this object
     */
    val description: String
}