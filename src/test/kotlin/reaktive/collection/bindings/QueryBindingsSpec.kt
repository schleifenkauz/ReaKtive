/**
 *@author Nikolaus Knop
 */

package reaktive.collection.bindings

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.collection.binding.find
import reaktive.random.Gen
import reaktive.set.reactiveSet
import reaktive.value.binding.testBinding

internal object QueryBindingsSpec : Spek({
    Feature("query bindings"){
        Scenario("find binding") {
            val set = reactiveSet(1, 2, 3, 4)
            val isEven = { x: Int -> x % 2 == 0 }
            val evenElement = set.find(isEven)
            fun expected() = set.now.find(isEven)
            testBinding(evenElement, ::expected) {
                with(set.now) {
                    "removing the found even element" {
                        remove(2)
                    }
                    "adding an odd element" {
                        add(3)
                    }
                    "removing another even element" {
                        remove(4)
                    }
                    "adding an even element" {
                        add(6)
                    }
                }
                repeat(10) {
                    mutateRandomly("the source", set.now, Gen.int(0, 1000))
                }
            }
        }
    }
})