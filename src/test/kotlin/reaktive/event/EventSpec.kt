/**
 *@author Nikolaus Knop
 */

package reaktive.event

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.event.mocks.TestEventHandler

internal object EventSpec: Spek({
    Feature("events") {
        Scenario("a unit event") {
            val e = event<Int>()
            val handler = TestEventHandler<Int>()
            val s = e.stream
            val observer = s.observe(handler)
            When("firing an event when a handler is added") {
                e.fire(34)
            }
            Then("should emit the event to the stream") {
                handler.testFired(s to 34)
            }
            When("firing an event after cancelling the observer") {
                observer.kill()
                e.fire(40)
            }
            Then("should not invoke the added handler") {
                handler.testFired(emptyList())
            }
        }
    }
})