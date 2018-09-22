/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value.impl

import org.nikok.reaktive.value.base.AbstractReactiveVariable

internal class ReactiveVariableImpl<T>(override val description: String, private var value: T) :
        AbstractReactiveVariable<T>() {
    override fun get(): T = value

    override fun doSet(value: T) {
        this.value = value
    }
}