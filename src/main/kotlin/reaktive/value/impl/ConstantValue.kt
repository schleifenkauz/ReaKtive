/**
 *@author Nikolaus Knop
 */

package reaktive.value.impl

import reaktive.value.base.AbstractValue

internal class ConstantValue<T>(private val value: T) : AbstractValue<T>() {
    override fun get(): T = value
}