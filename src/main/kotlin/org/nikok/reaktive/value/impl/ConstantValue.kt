/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.value.impl

import org.nikok.reaktive.value.base.AbstractValue

internal class ConstantValue<T>(override val description: String, private val value: T) : AbstractValue<T>() {
    override fun get(): T = value
}