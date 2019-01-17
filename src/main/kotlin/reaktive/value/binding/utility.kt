/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.value.ReactiveValue
import reaktive.value.reactiveValue

fun <T> ReactiveValue<T>.takeIf(pred: (T) -> Boolean) = map { it.takeIf(pred) }

fun <T> ReactiveValue<T>.orElse(then: ReactiveValue<T>) = flatMap {
    if (it != null) reactiveValue(it)
    else then
}

