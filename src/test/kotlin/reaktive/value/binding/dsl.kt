/**
 *@author Nikolaus Knop
 */

package reaktive.value.binding

import com.natpryce.hamkrest.equalTo
import org.spekframework.spek2.style.gherkin.ScenarioBody
import reaktive.binding.AbstractBindingsTestBody
import reaktive.util.shouldBe
import reaktive.value.now

internal fun <T> ScenarioBody.testBinding(
    binding: Binding<T>,
    expectedValue: () -> T,
    block: BindingTestBody<T>.() -> Unit
) {
    val body = BindingTestBody(this, binding, expectedValue)
    with(body) {
        "initially" { }
        block()
    }
}

internal class BindingTestBody<T>(
    scenario: ScenarioBody,
    private val binding: Binding<T>,
    private val expectedValue: () -> T
) : AbstractBindingsTestBody(scenario) {
    override fun ScenarioBody.check() {
        Then("it should be update correctly") {
            binding.now shouldBe equalTo(expectedValue())
        }
    }
}