/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.value.ReactiveValue

fun <C : Comparable<O>, O> ReactiveValue<C>.compareTo(other: O): Binding<Int> = map { v -> v.compareTo(other) }

fun <C : Comparable<O>, O> ReactiveValue<C>.greaterThan(other: O): Binding<Boolean> = compareTo(other).map { it > 0 }

fun <C : Comparable<O>, O> ReactiveValue<C>.lessThan(other: O): Binding<Boolean> = compareTo(other).map { it < 0 }

fun <C : Comparable<O>, O> ReactiveValue<C>.greaterThanOrEqualTo(other: O): Binding<Boolean> =
    compareTo(other).map { it >= 0 }

fun <C : Comparable<O>, O> ReactiveValue<C>.lessThanOrEqualTo(other: O): Binding<Boolean> =
    compareTo(other).map { it <= 0 }