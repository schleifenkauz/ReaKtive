/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value.help

import com.natpryce.hamkrest.Matcher
import org.nikok.reaktive.value.Value

fun <T> value(matcher: Matcher<T>): Matcher<Value<T>> {
    return Matcher("holds a value that ${matcher.description}") { matcher.asPredicate().invoke(it.get()) }
}

