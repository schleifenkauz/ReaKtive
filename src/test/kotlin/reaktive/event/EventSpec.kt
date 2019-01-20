/**
 *@author Nikolaus Knop
 */

package reaktive.event

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import reaktive.event.mocks.TestEventHandler

internal object EventSpec: Spek({
    given("a unit event") {
        val e = event<Int>()
        val handler = TestEventHandler<Int>()
        val s = e.stream
        val subscription = s.subscribe(handler)
        on("firing an event when a handler is added") {
            e.fire(34)
            it("should emit the event to the stream") {
                handler.testFired(s to 34)
            }
        }
        on("firing an event after cancelling the subscription") {
            subscription.cancel()
            e.fire(40)
            it("should not invoke the added handler") {
                handler.testFired(emptyList())
            }
        }
    }
})