/**
 *@author Nikolaus Knop
 */

package reaktive.list

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import reaktive.util.testSameEffects

object ReactiveListSpec : Spek({
    given("a reactive list") {
        describe("mutating") {
            val l = reactiveList(0, 1, 2)
            val test = mutableListOf(0, 1, 2)
            testSameEffects(test, l.now) {
                "add an element at tail" {

                }
                "add an element at a specified index" {

                }
                "add an element at the head" {

                }
                "remove an element at a specified index" {

                }
                "remove an element" {

                }
                "remove an element not present in the list" {

                }
                "add many elements" {

                }
                "add many elements at a specified index" {

                }
            }

        }
    }
})