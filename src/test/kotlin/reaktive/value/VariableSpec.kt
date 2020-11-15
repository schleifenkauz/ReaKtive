/**
 *@author Nikolaus Knop
 */

package reaktive.value

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.Observer
import reaktive.util.*
import reaktive.util.value

internal object VariableSpec : Spek({
    val v = variable(1)
    include(VariableSpec.common(v))
}) {
    fun common(v: Variable<Int>): Spek = spek {
        Feature("variable") {
            Scenario("set") {
                When("setting it to 2") {
                    v.set(2)
                }
                Then("should return 2 When getting the value") {
                    v shouldMatch value(equalTo(2))
                }
            }
            Scenario("bind") {
                val rv = reactiveVariable(3)
                lateinit var obs: Observer
                When("binding it to a reactive value with value 3") {
                    obs = v.bind(rv)
                }
                Then("it should set the value to 3") {
                    v shouldMatch value(equalTo(3))
                }
                Then("it should be bound") {
                    v.isBound shouldBe `true`
                }
                When("then setting the bound reactive value to 4") {
                    rv.set(4)
                }
                Then("it should set the value of the variable to 4") {
                    v shouldMatch value(equalTo(4))
                }
                When("killing the observer and then setting the reactive value to 5") {
                    obs.kill()
                    rv.set(5)
                }
                Then("it should not update the variable") {
                    v shouldMatch value(equalTo(4))
                }
                Then("it should not be bound") {
                    v.isBound shouldMatch `false`
                }
                lateinit var obs2: Observer
                lateinit var obs3: Observer
                When("binding the variable and then setting it") {
                    obs2 = v.bind(reactiveValue(5))
                }
                Then("it should throw an AlreadyBoundException") {
                    { v.set(1209) } shouldMatch throws<AlreadyBoundException>()
                    obs2.kill()
                }
                When("binding the variable and again binding it") {
                    obs3 = v.bind(reactiveValue(6))
                }
                Then("it should throw an AlreadyBoundException") {
                    val rv2 = reactiveValue(7);
                    { v.bind(rv2); Unit } shouldMatch throws<AlreadyBoundException>()
                    obs3.kill()
                }
            }
        }
    }
}