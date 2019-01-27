/**
 *@author Nikolaus Knop
 */

package reaktive.value.base

import reaktive.value.Value

/**
 * Abstract base class for [Value]s implementing string representation
 * @constructor
 * @return a new [AbstractValue]
 */
abstract class AbstractValue<T> : Value<T> {
    final override fun toString(): String = "${javaClass.simpleName}(now=${get()})"
}