/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value

import org.nikok.reaktive.Described

/**
 * A Handler that is used to observe the value of an [ReactiveValue] of type [T]
 */
interface ValueChangeHandler<in T> : Described {

    /**
     * Called when the value of [rv] has changed from [old] to [new]
     */
    fun valueChanged(rv: ReactiveValue<T>, old: T, new: T)
}