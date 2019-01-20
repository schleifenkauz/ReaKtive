/**
 *@author Nikolaus Knop
 */

package reaktive

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import reaktive.mocks.TestInvalidationHandler
import reaktive.set.reactiveSet
import reaktive.value.reactiveVariable

internal object DependenciesSpec : Spek({
    describe("no dependencies") {
        val deps = noDependencies()
        test("they should never fire invalidation") {
            deps.observe { throw AssertionError("no dependencies should not be invalidated") }
        }
    }
    describe("constant dependencies") {
        val dep1 = reactiveVariable(0)
        val dep2 = reactiveVariable(1)
        val deps = dependencies(dep1, dep2)
        val handler = TestInvalidationHandler()
        deps.observe(handler)
        on("invalidating a child reactive") {
            dep1.set(10)
            it("should fire invalidation") {
                handler.testInvalidatedInOrder(deps)
            }
        }
    }
    describe("dynamic dependencies") {
        val dep1 = reactiveVariable(0)
        val dep2 = reactiveVariable(1)
        val dep3 = reactiveVariable(2)
        val set = reactiveSet(dep1, dep2)
        val deps = Dependencies.dynamic(set)
        val handler = TestInvalidationHandler()
        deps.observe(handler)
        on("invalidating a child reactive") {
            dep1.set(2)
            it("should fire invalidation") {
                handler.testInvalidatedInOrder(deps)
            }
        }
        on("removing a child dependency and then invalidating the removed reactive") {
            set.now.remove(dep2)
            dep2.set(-2)
            it("should not fire") {
                handler.testInvalidatedInOrder(emptyList())
            }
        }
        on("adding a child dependency and then invalidating the added reactive") {
            set.now.add(dep3)
            dep3.set(-3)
            it("should fire") {
                handler.testInvalidatedInOrder(deps)
            }
        }
    }
})