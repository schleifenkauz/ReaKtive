/**
 *@author Nikolaus Knop
 */

package reaktive.binding

import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.ScenarioBody
import reaktive.random.Gen

internal abstract class AbstractBindingsTestBody(private val scenario: ScenarioBody) {
    @PublishedApi internal abstract fun ScenarioBody.check()

    operator fun String.invoke(action: TestBody.() -> Unit) {
        scenario.When(this, body = action)
        scenario.check()
    }

    fun <T> mutateRandomly(description: String, collection: MutableCollection<in T>, generator: Gen<T>) {
        val addOrRemove = Gen.int(0, 2).next()
        if (addOrRemove == 0 || collection.isEmpty()) {
            val element = generator.next()
            "adding $element to $description" {
                collection.add(element)
            }
        } else {
            val element = Gen.fromList(collection.toList()).next()
            "removing $element from $description" {
                collection.remove(element)
            }
        }
    }
}
