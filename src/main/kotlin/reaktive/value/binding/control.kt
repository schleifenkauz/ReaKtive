/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding

import reaktive.dependencies
import reaktive.value.*

fun <T> `if`(cond: ReactiveBoolean, then: () -> T, otherwise: () -> T): Binding<T> =
    cond.map { b -> if (b) then() else otherwise() }

fun <T> `if`(cond: ReactiveBoolean, then: ReactiveValue<T>, otherwise: ReactiveValue<T>): Binding<T> =
    binding<T>(dependencies(cond, then, otherwise)) { if (cond.now) then.now else otherwise.now }
