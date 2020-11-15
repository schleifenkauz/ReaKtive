/**
 *@author Nikolaus Knop
 */

package reaktive.mocks

import com.natpryce.hamkrest.equalTo
import reaktive.InvalidationHandler
import reaktive.Reactive
import reaktive.util.shouldMatch
import java.util.*

internal class TestInvalidationHandler : InvalidationHandler {
    private val invalidatedReactives = LinkedList<Reactive>()

    override fun invoke(invalidated: Reactive) {
        invalidatedReactives.add(invalidated)
    }

    fun testInvalidatedInOrder(reactives: List<Reactive>) {
        invalidatedReactives shouldMatch equalTo(reactives)
        invalidatedReactives.clear()
    }

    fun testInvalidatedInOrder(vararg reactives: Reactive) = testInvalidatedInOrder(reactives.asList())
}
