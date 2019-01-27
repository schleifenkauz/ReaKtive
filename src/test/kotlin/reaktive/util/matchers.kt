/**
 * @author Nikolaus Knop
 */

package reaktive.util

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.should.shouldMatch
import com.natpryce.hamkrest.throws
import org.jetbrains.spek.api.dsl.*
import kotlin.reflect.KProperty1

infix fun <T> T.shouldBe(matcher: Matcher<T>) = shouldMatch(matcher)

val `true` = Matcher<Boolean?>("is true") { it == true }

val `false` = Matcher<Boolean?>("is false") { it == false }

val `null` = Matcher<Any?>("is null") { it == null }

val anything = Matcher<Any?>("is anything") { true }

inline fun <reified T : Throwable> throwing(exceptionCriteria: Matcher<T> = anything): Matcher<() -> Unit> =
    throws(exceptionCriteria)

fun <R, T> KProperty1<R, T>.matches(matcher: Matcher<T>): Matcher<R> {
    return Matcher("has property ${this.name} ${matcher.description}") { matcher.asPredicate().invoke(get(it)) }
}

fun message(content: String?): Matcher<Throwable> {
    return Matcher("has message $content") { it.message == content }
}

fun fail(msg: String, cause: Throwable? = null): Nothing = throw AssertionError(msg, cause)

inline fun <T : Any, R> TestContainer.assertSameEffect(test: T, actual: T, crossinline action: T.() -> R) {
    runCatching { test.action() }
        .onFailure { ex ->
            val exCls = ex.javaClass
            val exClsName = exCls.name
            it("should throw a $exClsName") {
                runCatching { actual.action() }
                    .onFailure { actualEx ->
                        if (actualEx.javaClass != exCls) {
                            fail("was expected to throw $exClsName but threw ${exCls.name}", actualEx)
                        }
                    }
                    .onSuccess { ret ->
                        fail("was expected to throw $exClsName but returned $ret")
                    }
            }
        }
        .onSuccess { expectedRet ->
            it("should return $expectedRet") {
                runCatching { actual.action() }
                    .onFailure { ex ->
                        fail("should return $expectedRet but threw ${ex.javaClass.name}", ex)
                    }
                    .onSuccess { actualRet ->
                        assert(actualRet == expectedRet) { "expected to return $expectedRet but returned $actualRet" }
                    }
            }
        }
    it("should be equal to $test") {
        assert(test == actual) { "Expected $test but was $actual" }
    }
}

internal class TestSameEffectsBody<T : Any>(private val spec: SpecBody, private val test: T, private val actual: T) {
    inline operator fun <R> String.invoke(crossinline action: T.() -> R) {
        spec.group("on $this") {
            assertSameEffect(test, actual, action)
        }
    }
}

internal inline fun <T : Any> SpecBody.testSameEffects(test: T, actual: T, body: TestSameEffectsBody<T>.() -> Unit) {
    TestSameEffectsBody(this, test, actual).body()
}