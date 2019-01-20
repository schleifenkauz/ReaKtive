/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.dependencies
import reaktive.value.ReactiveValue
import reaktive.value.now

fun <T, F> binding(
    op1: ReactiveValue<T>,
    op2: ReactiveValue<T>,
    operator: (T, T) -> F
) = binding<F>(dependencies(op1, op2)) { operator(op1.now, op2.now) }

fun <C : Comparable<O>, O> ReactiveValue<C>.compareTo(other: O): Binding<Int> =
    map { v -> v.compareTo(other) }

fun <C : Comparable<O>, O> ReactiveValue<C>.greaterThan(other: O): Binding<Boolean> =
    compareTo(other).map { it > 0 }
