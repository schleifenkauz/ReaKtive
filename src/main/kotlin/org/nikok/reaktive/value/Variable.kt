/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value

import org.nikok.reaktive.Observer

/**
 * A variable of type [T] that can be set, get and bound
 */
interface Variable<T> : Value<T> {
    /**
     * Sets the value of this variable to [value]
     * @throws AlreadyBoundException if the variable is bound
     */
    fun set(value: T)

    /**
     * Binds this [Variable] to [other]
     * which means that this value will always have the same value as [other] until the returned [Observer] is killed
     * @throws AlreadyBoundException if the variable is already bound
     */
    fun bind(other: ReactiveValue<T>): Observer

    /**
     * @return `true` if and only if this [Variable] is bound to an [ReactiveValue]
     */
    val isBound: Boolean

    /**
     * @return the [VariableSetter] for this [Variable]
     * * Holding a reference to [setter] doesn't prevent this [Variable] from being finalized
     */
    val setter: VariableSetter<T>
}