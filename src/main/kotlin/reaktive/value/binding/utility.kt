/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.dependencies
import reaktive.value.*

fun <T> ReactiveValue<T>.equalTo(other: ReactiveValue<T>) =
    binding<Boolean>(dependencies(this, other)) { this.now == other.now }

fun <T> ReactiveValue<T>.equalTo(other: T) = map { it == other }

fun <T> ReactiveValue<T>.notEqualTo(other: ReactiveValue<T>) =
    binding<Boolean>(dependencies(this, other)) { this.now != other.now }

fun <T> ReactiveValue<T>.notEqualTo(other: T) = map { it != other }

fun <T> ReactiveValue<T>.takeIf(pred: (T) -> Boolean) = map { it.takeIf(pred) }

fun <T> ReactiveValue<T>.takeIf(boolean: ReactiveBoolean) = boolean.map { if (it) now else null }

fun <T> ReactiveValue<T?>.orElse(then: ReactiveValue<T>) = binding(this, then) { a, b -> a ?: b }

fun <T> ReactiveValue<T?>.orElse(then: T) = map { it ?: then }
