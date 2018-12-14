/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value.binding

import org.nikok.reaktive.dependencies
import org.nikok.reaktive.value.ReactiveValue
import org.nikok.reaktive.value.now

fun <T, F> binding(
    description: String,
    op1: ReactiveValue<T>,
    op2: ReactiveValue<T>,
    operator: (T, T) -> F
) = binding<F>(description, dependencies(op1, op2)) { operator(op1.now, op2.now) }

fun <C : Comparable<O>, O> ReactiveValue<C>.compareTo(other: O): Binding<Int> =
        map("$this compareTo $other") { v -> v.compareTo(other) }

fun <C : Comparable<O>, O> ReactiveValue<C>.greaterThan(other: O): Binding<Boolean> =
        compareTo(other).map("$this greater than $other") { it > 0 }
