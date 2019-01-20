/**
 * @author Nikolaus Knop
 */

package reaktive.value

import reaktive.Observer

/**
 * A [Variable] of type [T] which can be observed for changes
 */
interface ReactiveVariable<T> : ReactiveValue<T>, Variable<T> {
    /**
     * @return a [VariableSetter] which only holds a strong reference to this
     * [ReactiveVariable] when there are any unkilled observers of this variable
     */
    override val setter: VariableSetter<T>

    /**
     * Binds this [ReactiveVariable] to [other] such that until the returned observer is
     * killed both [ReactiveVariable]s always have the same value.
     * * A [ReactiveVariable] can have multiple bidirectional bindings
     */
    fun bindBidirectional(other: ReactiveVariable<T>): Observer
}