package reaktive.list

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.collection.binding.any
import reaktive.value.binding.testBinding
import reaktive.value.mocks.TestValueChangeHandler
import reaktive.value.now
import reaktive.value.reactiveVariable

object ExtensionsSpec : Spek({
    Feature("list extensions") {
        Scenario("observeEach") {
            val v1 = reactiveVariable(1)
            val v2 = reactiveVariable(2)
            val v3 = reactiveVariable(3)
            val list = reactiveList(v1, v2)
            val handler = TestValueChangeHandler<Int>()
            val o = list.observeEach { _, e -> e.observe(handler) }
            When("invalidating a value inside the set") {
                v1.set(3)
            }
            Then("it should invoke the handler") {
                handler.testValueChanged(v1, 1, 3)
            }
            When("adding a new variable to the set and changing it") {
                list.now.add(v3)
                v3.set(4)
            }
            Then("it should invoke the handler") {
                handler.testValueChanged(v3, 3, 4)
            }
            When("removing a variable from the set and changing it") {
                list.now.remove(v2)
                v2.set(5)
            }
            Then("it should not invoke the handler") {
                handler.testNoChange()
            }
            val v4 = reactiveVariable(4)
            When("adding a variable twice then removing it once and then invalidating it") {
                list.now.add(v4)
                list.now.add(v4)
                list.now.remove(v4)
                v4.set(7)
            }
            Then("it should invoke the handler once") {
                handler.testValueChanged(v4, 4, 7)
            }
            afterGroup { o.kill() }
        }
        Scenario("extra dependencies") {
            val v1 = reactiveVariable(1)
            val v2 = reactiveVariable(2)
            val v3 = reactiveVariable(3)
            val list = reactiveList(v1, v2)
            val exists3 = list.withDependencies { it }.any { it.now >= 3 }
            testBinding(exists3, { list.now.any { it.now >= 3 } }) {
                "add value 3" {
                    list.now.add(v3)
                }
                "modify value to not match predicate" {
                    v3.set(2)
                }
                "modify value to match predicate" {
                    v1.set(5)
                }
                "modify value to stay satisfying" {
                    v1.set(6)
                }
                "modify value to not match predicate" {
                    v1.set(0)
                }
                "modify value to stay unsatisfying" {
                    v1.set(-1)
                }
            }
        }
    }
})