/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.mocks

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.describedAs
import com.natpryce.hamkrest.should.shouldMatch
import org.nikok.reaktive.InvalidationHandler
import org.nikok.reaktive.Reactive
import java.util.*

internal class TestInvalidationHandler(override val description: String = "test") : InvalidationHandler {
    private val invalidatedReactives = LinkedList<Reactive>()

    override fun invalidated(invalidated: Reactive) {
        invalidatedReactives.add(invalidated)
    }

    fun testInvalidatedInOrder(reactives: List<Reactive>) {
        invalidatedReactives describedAs "the invalidated reactives" shouldMatch equalTo(reactives)
        invalidatedReactives.clear()
    }

    fun testInvalidated(reactives: List<Reactive>) {
        val sorted = invalidatedReactives.sortedBy { it.description }
        val reactivesSorted = reactives.sortedBy { it.description }
        sorted describedAs "the invalidated reactives" shouldMatch equalTo(reactivesSorted)
        invalidatedReactives.clear()
    }

    fun testInvalidatedInOrder(vararg reactives: Reactive) = testInvalidatedInOrder(reactives.asList())

    fun testInvalidated(vararg reactives: Reactive) = testInvalidated(reactives.asList())
}
