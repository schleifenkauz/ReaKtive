/**
 * @author Nikolaus Knop
 */

package reaktive.value.impl

import reaktive.value.base.AbstractReactiveVariable

internal class ReactiveVariableImpl<T>(private var value: T) :
    AbstractReactiveVariable<T>() {
    override fun get(): T = value

    override fun doSet(value: T) {
        this.value = value
    }
}