package reaktive.collection.bindings


import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.collection.binding.allR
import reaktive.collection.binding.count
import reaktive.random.Gen
import reaktive.set.reactiveSet
import reaktive.util.*
import reaktive.value.binding.map
import reaktive.value.binding.testBinding
import reaktive.util.value
import reaktive.value.reactiveVariable

internal object ExistenceBindingsSpec : Spek({
    Feature("existence bindings") {
        Scenario("all binding") {
            val first = reactiveVariable(1)
            val second = reactiveVariable(2)
            val third = reactiveVariable(3)
            val s = reactiveSet(
                first,
                second,
                third
            )
            val b = s.allR { v -> v.map { n -> n % 2 == 0 } }
            Then("it should initially be false") {
                b shouldMatch value(`false`)
            }
            When("setting both odd values to be even") {
                first.set(0)
                third.set(4)
            }
            Then("it should be true") {
                b shouldMatch value(`true`)
            }
            When("setting the even value to be odd") {
                second.set(1)
            }
            Then("should be false") {
                b shouldMatch value(`false`)
            }
            When("removing the odd value") {
                s.now.remove(second)
            }
            Then("should be true") {
                b shouldMatch value(`true`)
            }
            When("adding an odd value") {
                s.now.add(reactiveVariable(23))
            }
            Then("should be false") {
                b shouldMatch value(`false`)
            }
        }
        Scenario("count binding") {
            val set = reactiveSet(1, 2, 3, 4)
            val isEven = { x: Int -> x % 2 == 0 }
            val countEven = set.count(isEven)
            fun expected() = set.now.count(isEven)
            testBinding(countEven, ::expected) {
                with(set.now) {
                    "adding even element" { add(6) }
                    "removing even element" { remove(4) }
                    "adding odd element" { add(5) }
                    "removing odd element" { remove(3) }
                    repeat(10) {
                        mutateRandomly("the source", set.now, Gen.int(0, 1000))
                    }
                }
            }
        }
        Scenario("contains binding") {
            //TODO()
        }
    }
})