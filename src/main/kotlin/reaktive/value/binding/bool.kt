/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.dependencies
import reaktive.value.ReactiveBoolean
import reaktive.value.now

/**
 * @return a boolean binding holding the negation of this [ReactiveBoolean]
 */
operator fun ReactiveBoolean.not() = map { !it }

/**
 * @return a boolean binding holding `true` only if the value of this and the [other] [ReactiveBoolean] are both `true`
 */
infix fun ReactiveBoolean.and(other: ReactiveBoolean): Binding<Boolean> =
    binding<Boolean>(dependencies(this, other)) { now && other.now }

/**
 * @return a boolean binding holding `true` only if at least one of the operands holds `true`
 */
infix fun ReactiveBoolean.or(other: ReactiveBoolean): Binding<Boolean> =
    binding<Boolean>(dependencies(this, other)) { now || other.now }

/**
 * @return a boolean binding holding `true` only if exactly one of the operands holds `true`
 */
infix fun ReactiveBoolean.xor(other: ReactiveBoolean): Binding<Boolean> =
    binding<Boolean>(dependencies(this, other)) { now xor other.now }

/**
 * Equivalent to `this.and(reactiveValue(other))`
 */
infix fun ReactiveBoolean.and(other: Boolean): Binding<Boolean> =
    if (!other) constantBinding(false) else this.asBinding()

/**
 * Equivalent to `this.or(reactiveValue(other))`
 */
infix fun ReactiveBoolean.or(other: Boolean): Binding<Boolean> = if (other) constantBinding(false) else this.asBinding()

/**
 * Equivalent to `this.xor(reactiveValue(other))`
 */
infix fun ReactiveBoolean.xor(other: Boolean): Binding<Boolean> = if (other) !this else this.asBinding()
