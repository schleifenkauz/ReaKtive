/**
 *@author Nikolaus Knop
 */

package reaktive.binding.help

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.dsl.*
import reaktive.value.Variable
import reaktive.value.binding.Binding
import reaktive.value.help.value

@DslMarker private annotation class BindingTestDsl

@BindingTestDsl internal interface BindingTest {
    fun <R> String.invoke(action: () -> R)

    fun <R> test(description: String, action: () -> R)

    fun <T> set(variable: Variable<T>, body: VariableContext<T>.() -> Unit)

    @BindingTestDsl interface VariableContext<in T> {
        fun to(value: T)
        fun toAll(vararg values: T)
    }
}

internal fun <T> SpecBody.testBinding(binding: Binding<T>, criteria: () -> Matcher<T>, body: BindingTest.() -> Unit) {
    val bindingTest = BindingTestImpl(this, binding, criteria)
    bindingTest.body()
}

@JvmName("testBindingExpecting") internal fun <T> SpecBody.testBinding(
    binding: Binding<T>,
    expected: () -> T,
    body: BindingTest.() -> Unit
) {
    val criteria = { equalTo(expected()) }
    testBinding(binding, criteria, body)
}

private class BindingTestImpl<T>(
    private val body: SpecBody, private val binding: Binding<T>, private val valueCriteria: () -> Matcher<T>
) : BindingTest {
    init {
        body.it("should have a value that ${valueCriteria().description}") {
            binding shouldMatch value(valueCriteria())
        }
    }

    override fun <R> test(description: String, action: () -> R) {
        body.on(description) {
            action()
            it("should have a value that ${valueCriteria().description}") {
                binding shouldMatch value(valueCriteria())
            }
        }
    }

    override operator fun <R> String.invoke(action: () -> R) {
        test(this, action)
    }

    override fun <T> set(variable: Variable<T>, body: BindingTest.VariableContext<T>.() -> Unit) {
        val ctx = VariableContextImpl(variable)
        ctx.body()
    }

    private inner class VariableContextImpl<T>(private val v: Variable<T>) : BindingTest.VariableContext<T> {
        override fun to(value: T) {
            this@BindingTestImpl.test("set $v to $value") { v.set(value) }
        }

        override fun toAll(vararg values: T) {
            for (v in values) {
                to(v)
            }
        }
    }
}
