/**
 *@author Nikolaus Knop
 */

package reaktive.value.impl

import reaktive.Observer
import reaktive.getValue
import reaktive.value.*
import java.lang.ref.WeakReference

internal class VariableSetterImpl<in T>(v: Variable<T>) : VariableSetter<T> {
    private val variable by WeakReference(v)

    override fun set(value: T): Boolean {
        variable?.set(value)
        return variable != null
    }

    override fun bind(other: ReactiveValue<T>): Observer = variable?.bind(other) ?: Observer.nothing
}