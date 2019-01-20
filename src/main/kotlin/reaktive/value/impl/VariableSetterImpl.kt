/**
 *@author Nikolaus Knop
 */

package reaktive.value.impl

import org.nikok.kref.weak
import reaktive.Observer
import reaktive.value.*

internal class VariableSetterImpl<in T>(v: Variable<T>) : VariableSetter<T> {
    private val variable by weak(v)

    override fun set(value: T): Boolean {
        variable?.set(value)
        return variable != null
    }

    override fun bind(other: ReactiveValue<T>): Observer = variable?.bind(other) ?: Observer.nothing
}