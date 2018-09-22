/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value

import org.nikok.reaktive.value.impl.*

/**
 * @return a [Value] described by [description] with [value]
 */
fun <T> value(description: String, value: T): Value<T> = ConstantValue(description, value)

/**
 * @return a [Variable] described by [description] with initial value of [initial]
 */
fun <T> variable(description: String, initial: T): Variable<T> = VariableImpl(initial, description)

/**
 * @return a constant [ReactiveValue] described by [description] with [value]
 */
fun <T> reactiveValue(description: String, value: T): ReactiveValue<T> = ConstantReactiveValue(description, value)

/**
 * @return a [ReactiveVariable] described by [description] with initial value of [initial]
 */
fun <T> reactiveVariable(description: String, initial: T): ReactiveVariable<T> =
        ReactiveVariableImpl(description, initial)