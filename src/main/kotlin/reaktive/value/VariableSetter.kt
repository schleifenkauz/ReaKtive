/**
 * @author Nikolaus Knop
 */

package reaktive.value

import reaktive.Observer

/**
 * A [VariableSetter] only holds a [java.lang.ref.WeakReference] to its associated [Variable] and can only [set] the value
 */
interface VariableSetter<in T> {
    /**
     * * Sets the value of the associated [Variable] to [value]
     * * Has no effect when the associated [Variable] was already garbage collected
     * * Returns whether the value could be set or not
     */
    fun set(value: T): Boolean

    /**
     * Bind the associated [Variable] to [other] and return the resulting observer
     */
    fun bind(other: ReactiveValue<T>): Observer
}