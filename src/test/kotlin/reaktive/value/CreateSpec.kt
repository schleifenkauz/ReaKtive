package reaktive.value

import com.natpryce.hamkrest.equalTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.util.shouldMatch
import reaktive.util.value

internal object CreateSpec : Spek({
    fun testInitialValue(createValue: (Int) -> Value<Int>) {
        val v = createValue(1)
        v shouldMatch value(equalTo(1))
    }

    Feature("creating reactive variables and values") {
        Scenario("value(name, value)") {
            Then("it should return a Value of value described by name") {
                testInitialValue(::value)
            }
        }
        Scenario("variable(name, value)") {
            Then("it should return a Variable of value described by name") {
                testInitialValue(::variable)
            }
        }
        Scenario("reactiveValue(name, value)") {
            Then("it should return a ReactiveValue of value described by name") {
                testInitialValue(::reactiveValue)
            }
        }
        Scenario("reactiveVariable(name, value)") {
            Then("it should return a ReactiveVariable of value described by name") {
                testInitialValue(::reactiveVariable)
            }
        }
    }
})