/**
 *@author Nikolaus Knop
 */

package reaktive

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.mocks.TestInvalidationHandler
import reaktive.set.reactiveSet
import reaktive.value.reactiveVariable

internal object DependenciesSpec : Spek({
    Feature("dependencies "){
        Scenario("no dependencies") {
            val deps = noDependencies()
            Then("they should never fire invalidation") {
                deps.observe { throw AssertionError("no dependencies should not be invalidated") }
            }
        }
        Scenario("constant dependencies") {
            val dep1 = reactiveVariable(0)
            val dep2 = reactiveVariable(1)
            val deps = dependencies(dep1, dep2)
            val handler = TestInvalidationHandler()
            deps.observe(handler)
            When("invalidating a child reactive") {
                dep1.set(10)
            }
            Then("it should fire invalidation") {
                handler.testInvalidatedInOrder(deps)
            }
        }
        Scenario("dynamic dependencies") {
            val dep1 = reactiveVariable(0)
            val dep2 = reactiveVariable(1)
            val dep3 = reactiveVariable(2)
            val set = reactiveSet(dep1, dep2)
            val deps = Dependencies.dynamic(set)
            val handler = TestInvalidationHandler()
            deps.observe(handler)
            When("invalidating a child reactive") {
                dep1.set(2)
            }
            Then("it should fire invalidation") {
                handler.testInvalidatedInOrder(deps)
            }
            When("removing a child dependency and then invalidating the removed reactive") {
                set.now.remove(dep2)
                dep2.set(-2)
            }
            Then("it should not fire") {
                handler.testInvalidatedInOrder(emptyList())
            }
            When("adding a child dependency and then invalidating the added reactive") {
                set.now.add(dep3)
                dep3.set(-3)
            }
            Then("it should fire") {
                handler.testInvalidatedInOrder(deps)
            }
        }
    }
})