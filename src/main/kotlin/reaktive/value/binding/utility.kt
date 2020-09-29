/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.dependencies
import reaktive.value.*

fun <T> ReactiveValue<T>.equalTo(other: ReactiveValue<T>): Binding<Boolean> =
    binding<Boolean>(dependencies(this, other)) { this.now == other.now }

fun <T> ReactiveValue<T>.equalTo(other: T): Binding<Boolean> = map { it == other }

fun <T> ReactiveValue<T>.notEqualTo(other: ReactiveValue<T>): Binding<Boolean> =
    binding<Boolean>(dependencies(this, other)) { this.now != other.now }

fun <T> ReactiveValue<T>.notEqualTo(other: T): Binding<Boolean> = map { it != other }

fun <T> ReactiveValue<T>.takeIf(pred: (T) -> Boolean): Binding<T?> = map { it.takeIf(pred) }

fun <T> ReactiveValue<T>.takeIf(boolean: ReactiveBoolean): Binding<T?> = boolean.map { if (it) now else null }

fun <T> ReactiveValue<T?>.orElse(then: ReactiveValue<T>): Binding<T> = binding(this, then) { a, b -> a ?: b }

fun <T> ReactiveValue<T?>.orElse(then: T): Binding<T> = map { it ?: then }
