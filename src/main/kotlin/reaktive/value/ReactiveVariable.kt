/**
 * @author Nikolaus Knop
 */

package reaktive.value

import kotlinx.serialization.Serializable
import reaktive.Observer
import reaktive.impl.ReactiveVariableSerializer
import reaktive.impl.WeakReactive

/**
 * A [Variable] of type [T] which can be observed for changes
 */
@Serializable(with = ReactiveVariableSerializer::class)
interface ReactiveVariable<T> : ReactiveValue<T>, Variable<T> {
    /**
     * @return a [VariableSetter] which only holds a strong reference to this
     * [ReactiveVariable] when there are any unkilled observers of this variable
     */
    override val setter: VariableSetter<T>

    override val weak: WeakReactive<ReactiveVariable<T>>

    /**
     * Binds this [ReactiveVariable] to [other] such that until the returned observer is
     * killed both [ReactiveVariable]s always have the same value.
     * * A [ReactiveVariable] can have multiple bidirectional bindings
     */
    fun bindBidirectional(other: ReactiveVariable<T>): Observer
}