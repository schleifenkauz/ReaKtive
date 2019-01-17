/**
 * @author Nikolaus Knop
 */

package reaktive.value

/**
 * A value of type [T]
 */
interface Value<out T> {
    /**
     * @return the value
     */
    fun get(): T

    /**
     * @return a String containing the description and value of this [Value]
     */
    override fun toString(): String
}