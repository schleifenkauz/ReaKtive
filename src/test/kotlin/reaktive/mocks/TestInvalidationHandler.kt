/**
 *@author Nikolaus Knop
 */

package reaktive.mocks

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.describedAs
import com.natpryce.hamkrest.should.shouldMatch
import reaktive.InvalidationHandler
import reaktive.Reactive
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
