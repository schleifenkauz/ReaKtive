/**
 * @author Nikolaus Knop
 */

package reaktive.help

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.should.shouldMatch
import com.natpryce.hamkrest.throws
import kotlin.reflect.KProperty1

infix fun <T> T.shouldBe(matcher: Matcher<T>) = shouldMatch(matcher)

val `true` = Matcher<Boolean?>("is true") { it == true }

val `false` = Matcher<Boolean?>("is false") { it == false }

val `null` = Matcher<Any?>("is null") { it == null }

val anything = Matcher<Any?>("is anything") { true }

inline fun <reified T : Throwable> throwing(exceptionCriteria: Matcher<T>): Matcher<() -> Unit> =
        throws(exceptionCriteria)

fun <R, T> KProperty1<R, T>.matches(matcher: Matcher<T>): Matcher<R> {
    return Matcher("has property ${this.name} ${matcher.description}") { matcher.asPredicate().invoke(get(it)) }
}

fun message(content: String?): Matcher<Throwable> {
    return Matcher("has message $content") { it.message == content }
}
