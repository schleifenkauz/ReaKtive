/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.value.mocks

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.should.describedAs
import com.natpryce.hamkrest.should.shouldMatch
import org.nikok.reaktive.help.anything
import org.nikok.reaktive.value.ReactiveValue
import org.nikok.reaktive.value.ValueChangeHandler

internal class TestValueChangeHandler<T>(override val description: String = "test") : ValueChangeHandler<T> {
    private var lastChange: ValueChange<T>? = null

    override fun valueChanged(rv: ReactiveValue<T>, old: T, new: T) {
        lastChange = ValueChange(rv, old, new)
    }

    fun testValueChanged(
        rv: Matcher<ReactiveValue<T>> = anything,
        old: Matcher<T> = anything,
        new: Matcher<T> = anything
    ) {
        val (actualRv, actualOld, actualNew) = lastChange ?: error("Expected a value change but absent")
        actualRv describedAs "the change value" shouldMatch rv
        actualOld describedAs "the old value" shouldMatch old
        actualNew describedAs "the new value" shouldMatch new
        lastChange = null
    }

    fun testValueChanged(rv: ReactiveValue<T>, old: T, new: T) {
        testValueChanged(equalTo(rv), equalTo(old), equalTo(new))
    }

    fun testNoChange() {
        lastChange describedAs "the last change" shouldMatch absent<ValueChange<T>>()
        lastChange = null
    }
}