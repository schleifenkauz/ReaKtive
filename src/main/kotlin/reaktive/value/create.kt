/**
 * @author Nikolaus Knop
 */

package reaktive.value

import reaktive.value.impl.*

/**
 * @return a [Value] with the specified [value]
 */
fun <T> value(value: T): Value<T> = ConstantValue(value)

/**
 * @return a [Variable] with initial value of [initial]
 */
fun <T> variable(initial: T): Variable<T> = VariableImpl(initial)

/**
 * @return a constant [ReactiveValue] with the specified [value]
 */
fun <T> reactiveValue(value: T): ReactiveValue<T> = ConstantReactiveValue(value)

/**
 * @return a [ReactiveVariable] with initial value of [initial]
 */
fun <T> reactiveVariable(initial: T): ReactiveVariable<T> =
    ReactiveVariableImpl(initial)