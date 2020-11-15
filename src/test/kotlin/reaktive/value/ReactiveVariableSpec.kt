/**
 *@author Nikolaus Knop
 */

package reaktive.value

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.Observer
import reaktive.value.binding.*
import reaktive.value.mocks.TestValueChangeHandler

internal object ReactiveVariableSpec : Spek({
    include(VariableSpec.common(reactiveVariable(1)))
    Feature("reactive variable") {
        val v = reactiveVariable(1)
        val handler = TestValueChangeHandler<Int>()
        lateinit var obs: Observer
        Scenario("observing and setting"){
            When("observing the variable and then setting it") {
                obs = v.observe(handler)
                v.set(3)
            }
            Then("it should invoke the handler") {
                handler.testValueChanged(v, 1, 3)
            }
            When("setting the value to the old value") {
                v.set(v.get())
            }
            Then("it should not invoke the handler") {
                handler.testNoChange()
            }
            When("stopping the observation and then setting the value") {
                obs.kill()
                v.set(4)
            }
            Then("it should not invoke the handler") {
                handler.testNoChange()
            }
        }
        Scenario("map") {
            val square = v.map { it * it }
            fun expected() = v.now * v.now
            testBinding(square, ::expected) {
                "set source to 3" {
                    v.set(3)
                }
                "set source -2" {
                    v.set(-2)
                }
            }
        }
        Scenario("flatMap") {
            val another = reactiveVariable(3)
            val product = v.flatMap { vNow ->
                another.map { anotherNow -> anotherNow * vNow }
            }

            fun expected() = another.now * v.now
            testBinding(product, ::expected) {
                "set another to 34" { another.set(34) }
                "set v to 3" { v.set(3) }
                "set another to -5" { another.set(-5) }
            }
        }
    }
})