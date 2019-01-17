/**
 *@author Nikolaus Knop
 */

package reaktive.value.binding

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.dsl.*
import reaktive.value.now

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