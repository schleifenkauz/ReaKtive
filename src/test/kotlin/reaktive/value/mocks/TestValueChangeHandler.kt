/**
 *@author Nikolaus Knop
 */

package reaktive.value.mocks

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.should.describedAs
import com.natpryce.hamkrest.should.shouldMatch
import reaktive.util.anything
import reaktive.value.ReactiveValue
import reaktive.value.ValueChangeHandler

internal class TestValueChangeHandler<T> : ValueChangeHandler<T> {
    private var lastChange: ValueChange<T>? = null

    override fun invoke(rv: ReactiveValue<T>, old: T, new: T) {
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