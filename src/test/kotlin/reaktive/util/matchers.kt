/**
 * @author Nikolaus Knop
 */

package reaktive.util

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.throws
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Root
import org.spekframework.spek2.style.gherkin.ScenarioBody
import reaktive.value.Value
import kotlin.reflect.KProperty1

infix fun <T> T.shouldBe(matcher: Matcher<T>) = assertThat(this, matcher)

infix fun <T> T.shouldMatch(matcher: Matcher<T>) = assertThat(this, matcher)

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

inline fun <T : Any, R> ScenarioBody.assertSameEffect(test: T, actual: T, crossinline action: T.() -> R) {
    runCatching { test.action() }
        .onFailure { ex ->
            val exCls = ex.javaClass
            val exClsName = exCls.name
            Then("it should throw a $exClsName") {
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
            Then("it should return $expectedRet") {
                runCatching { actual.action() }
                    .onFailure { ex ->
                        fail("should return $expectedRet but threw ${ex.javaClass.name}", ex)
                    }
                    .onSuccess { actualRet ->
                        assert(actualRet == expectedRet) { "expected to return $expectedRet but returned $actualRet" }
                    }
            }
        }
    Then("should be equal to $test") {
        assert(test == actual) { "Expected $test but was $actual" }
    }
}

internal class TestSameEffectsBody<T : Any>(
    private val scenario: ScenarioBody,
    private val test: T,
    private val actual: T
) {
    inline operator fun <R> String.invoke(crossinline action: T.() -> R) = with(scenario) {
        When(this@invoke) {}
        assertSameEffect(test, actual, action)
    }
}

internal inline fun <T : Any> ScenarioBody.testSameEffects(
    test: T,
    actual: T,
    body: TestSameEffectsBody<T>.() -> Unit
) {
    TestSameEffectsBody(this, test, actual).body()
}

fun <T> value(matcher: Matcher<T>): Matcher<Value<T>> =
    Matcher("holds a value that ${matcher.description}") { matcher.asPredicate().invoke(it.get()) }

data class Described<T>(val value: T, val description: String) {
    override fun toString(): String = description
}

infix fun <T> T.describedAs(description: String) = Described(this, description)

infix fun <T> Described<T>.shouldMatch(matcher: Matcher<T>) {
    value shouldMatch matcher
}

fun spek(body: Root.() -> Unit) = object : Spek(body) {}