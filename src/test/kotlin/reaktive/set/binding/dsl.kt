/**
 * @author Nikolaus Knop
 */

package reaktive.set.binding

import com.natpryce.hamkrest.equalTo
import org.spekframework.spek2.style.gherkin.ScenarioBody
import reaktive.binding.AbstractBindingsTestBody
import reaktive.util.shouldMatch


internal fun <E> ScenarioBody.testSetBinding(
    binding: SetBinding<E>,
    expected: () -> Set<E>,
    block: SetBindingTestBody<E>.() -> Unit
) {
    val body = SetBindingTestBody(this, binding, expected)
    with(body) {
        "initially" {}
        block()
    }
}

internal class SetBindingTestBody<E>(
    scenario: ScenarioBody,
    private val tested: SetBinding<E>,
    private val expectedSet: () -> Set<E>
) : AbstractBindingsTestBody(scenario) {
    override fun ScenarioBody.check() {
        Then("it should update correctly") {
            tested.now shouldMatch equalTo(expectedSet())
        }
    }
}