/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.value.impl

import org.nikok.reaktive.value.base.AbstractVariable

internal class VariableImpl<T>(private var value: T, override val description: String) : AbstractVariable<T>() {
    override fun doSet(value: T) {
        this.value = value
    }

    override fun get(): T = value
}