/**
 * @author Nikolaus Knop
 */

package reaktive.value.help

import com.natpryce.hamkrest.Matcher
import reaktive.value.Value

fun <T> value(matcher: Matcher<T>): Matcher<Value<T>> {
    return Matcher("holds a value that ${matcher.description}") { matcher.asPredicate().invoke(it.get()) }
}

