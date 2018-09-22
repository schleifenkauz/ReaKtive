/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.value.base

import org.nikok.reaktive.value.Value

/**
 * Abstract base class for [Value]s implementing string representation
 * @constructor
 * @return a new [AbstractValue]
 */
abstract class AbstractValue<T> : Value<T> {
    final override fun toString(): String {
        val v = get()
        return "$description: $v"
    }
}