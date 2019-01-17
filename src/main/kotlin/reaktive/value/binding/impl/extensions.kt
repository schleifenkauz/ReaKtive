/**
 * @author Nikolaus Knop
 */

package reaktive.value.binding.impl

import reaktive.value.ReactiveBoolean
import reaktive.value.ReactiveValue

/**
 * @return a [ReactiveBoolean] that holds `true` only if the value hold by this [ReactiveValue] is not null
 */
fun <T> ReactiveValue<T>.notNull(): ReactiveBoolean = map { it != null }