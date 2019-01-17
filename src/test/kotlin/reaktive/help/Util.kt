/**
 * @author Nikolaus Knop
 */

package reaktive.help

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

val todo = object : ReadOnlyProperty<Any?, Nothing> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Nothing {
        TODO("not implemented")
    }
}