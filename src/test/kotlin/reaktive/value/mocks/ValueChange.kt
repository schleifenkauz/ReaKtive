/**
 *@author Nikolaus Knop
 */

package reaktive.value.mocks

import reaktive.value.ReactiveValue

internal data class ValueChange<T>(val rv: ReactiveValue<T>, val old: T, val new: T)