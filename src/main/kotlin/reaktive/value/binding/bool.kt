/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.dependencies
import reaktive.value.ReactiveBoolean
import reaktive.value.now

operator fun ReactiveBoolean.not() = map { !it }

infix fun ReactiveBoolean.and(other: ReactiveBoolean): Binding<Boolean> =
    binding<Boolean>(dependencies(this, other)) { now && other.now }

infix fun ReactiveBoolean.or(other: ReactiveBoolean): Binding<Boolean> =
    binding<Boolean>(dependencies(this, other)) { now || other.now }

infix fun ReactiveBoolean.xor(other: ReactiveBoolean): Binding<Boolean> =
    binding<Boolean>(dependencies(this, other)) { now xor other.now }

infix fun ReactiveBoolean.and(other: Boolean): Binding<Boolean> = if (!other) binding(false) else this.asBinding()

infix fun ReactiveBoolean.or(other: Boolean): Binding<Boolean> = if (other) binding(false) else this.asBinding()

infix fun ReactiveBoolean.xor(other: Boolean): Binding<Boolean> = if (other) !this else this.asBinding()
