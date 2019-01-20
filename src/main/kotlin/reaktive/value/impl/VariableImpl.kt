/**
 *@author Nikolaus Knop
 */

package reaktive.value.impl

import reaktive.value.base.AbstractVariable

internal class VariableImpl<T>(private var value: T) : AbstractVariable<T>() {
    override fun doSet(value: T) {
        this.value = value
    }

    override fun get(): T = value
}