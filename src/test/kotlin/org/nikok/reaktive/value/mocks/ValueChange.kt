/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.value.mocks

import org.nikok.reaktive.value.ReactiveValue

internal data class ValueChange<T>(val rv: ReactiveValue<T>, val old: T, val new: T)