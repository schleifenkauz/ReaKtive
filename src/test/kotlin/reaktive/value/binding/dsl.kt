/**
 *@author Nikolaus Knop
 */

package reaktive.value.binding

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.dsl.*
import reaktive.Reactive
import reaktive.value.now

internal inline fun <T> testBinding(
    dependencies: Reactive,
    binding: Binding<T>,
    crossinline check: (T) -> Unit,
    actions: () -> Unit
) {
    val obs = dependencies.observe { check(binding.now) }
    actions()
    obs.kill()
}

@JvmName("testBindingWithCriteria")
internal inline fun <T> Spec.testBinding(
    dependencies: Reactive,
    binding: Binding<T>,
    crossinline criteria: () -> Matcher<T>,
    actions: () -> Unit
) = testBinding(dependencies, binding, { now ->
    test("it should update correctly") {
        now shouldMatch criteria()
    }
}, actions)

internal inline fun <T> Spec.testBinding(
    dependencies: Reactive,
    binding: Binding<T>,
    crossinline expected: () -> T,
    actions: () -> Unit
) = testBinding(
    dependencies,
    binding,
    { now -> it("should update correctly") { now shouldMatch equalTo(expected()) } },
    actions
)

internal inline fun <T> Spec.testBinding(
    binding: Binding<T>,
    noinline expectedValue: () -> T,
    body: BindingTestBody<T>.() -> Unit
) {
    BindingTestBody(this, binding, expectedValue).body()
}

internal class BindingTestBody<T>(
    private val spec: Spec,
    private val binding: Binding<T>,
    private val expectedValue: () -> T
) {
    inline operator fun String.invoke(crossinline action: () -> Unit) {
        spec.on(this) {
            action()
            val expected = expectedValue()
            it("should be $expected") {
                binding.now shouldMatch equalTo(expected)
            }
        }
    }
}