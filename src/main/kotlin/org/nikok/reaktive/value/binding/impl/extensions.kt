/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value.binding.impl

import org.nikok.reaktive.value.ReactiveValue

fun <T> ReactiveValue<T>.notNull() = map("$this is not null") { it != null }