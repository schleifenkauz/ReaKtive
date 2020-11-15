/**
 *@author Nikolaus Knop
 */

package reaktive.collection.bindings

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe
import reaktive.collection.binding.sum
import reaktive.random.Gen
import reaktive.set.reactiveSet
import reaktive.value.binding.testBinding

internal object NumericBindingsSpec : Spek({
    Feature("numeric bindings") {
        Scenario("sum binding") {
            val set = reactiveSet(1, 2, 3, 4)
            val sum = set.sum()
            fun expected() = set.now.sum()
            testBinding(sum, ::expected) {
                with(set.now) {
                    "adding value" {
                        add(7)
                    }
                    "adding negative value" {
                        add(-3)
                    }
                    "removing value" {
                        remove(4)
                    }
                    "removing negative value" {
                        remove(-3)
                    }
                }
                repeat(10) {
                    mutateRandomly("the source", set.now, Gen.int(0, 1000))
                }
            }
        }
    }
})