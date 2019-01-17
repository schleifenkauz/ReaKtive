/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.BindingBody
import reaktive.Observer
import reaktive.value.ReactiveValue
import reaktive.value.ReactiveVariable

/**
 * * Used to setup the associated [Binding]
 * * Only uses the [ReactiveVariable.setter] of the associated [ReactiveVariable] meaning that it could be garbage collected
 * when they are no observers or strong references anymore to it
 */
interface ValueBindingBody<T>: BindingBody {
    /**
     * Sets the value of the associated [Binding] to [value]
     */
    fun set(value: T)

    /**
     * Binds the value of the associated [Binding] to [other]
     * @return the resulting [Observer]
     */
    fun bind(other: ReactiveValue<T>): Observer

    /**
     * Executes [use] feeding the current value to it only if the associated [Binding] was not garbage collected before
     */
    fun withValue(use: (value: T) -> Unit)
}