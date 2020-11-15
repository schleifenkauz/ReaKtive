/**
 * @author Nikolaus Knop
 */

package reaktive.list

import com.natpryce.hamkrest.equalTo
import org.spekframework.spek2.style.gherkin.ScenarioBody
import reaktive.binding.AbstractBindingsTestBody
import reaktive.list.binding.ListBinding
import reaktive.util.shouldBe

internal fun <E> ScenarioBody.testListBinding(
    binding: ListBinding<E>,
    expected: () -> List<E>,
    block: ListBindingTestBody<E>.() -> Unit
) {
    val body = ListBindingTestBody(this, binding, expected)
    with(body) {
        "initially" {}
        block()
    }
}

internal class ListBindingTestBody<E>(
    scenario: ScenarioBody,
    private val tested: ListBinding<E>,
    private val expectedList: () -> List<E>
) : AbstractBindingsTestBody(scenario) {
    override fun ScenarioBody.check() {
        Then("it should update the list correctly") {
            tested.now shouldBe equalTo(expectedList())
        }
    }
}