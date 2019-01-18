/**
 *@author Nikolaus Knop
 */

package reaktive.collection.bindings

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import reaktive.collection.binding.sum
import reaktive.set.reactiveSet
import reaktive.value.binding.testBinding

internal object NumericBindingsSpec : Spek({
    describe("sum") {
        val set = reactiveSet(1, 2, 3, 4)
        val sum = set.sum()
        fun expected() = set.now.sum()
        testBinding(sum, ::expected) {
            with(set.now) {
                "add value" {
                    add(7)
                }
                "add negative value" {
                    add(-3)
                }
                "remove value" {
                    remove(4)
                }
                "remove negative value" {
                    remove(-3)
                }
            }
        }
    }
})