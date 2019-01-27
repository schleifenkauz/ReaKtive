/**
 *@author Nikolaus Knop
 */

package reaktive.list

import com.natpryce.hamkrest.should.describedAs
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import reaktive.random.Gen
import reaktive.util.testSameEffects

object ReactiveListSpec : Spek({
    given("a reactive list") {
        describe("mutating") {
            val l = reactiveList(0, 1, 2)
            val test = mutableListOf(0, 1, 2)
            testSameEffects(test, l.now) {
                "add an element at tail" {
                    add(3)
                }
                "add an element at a specified index" {
                    add(2, 4)
                }
                "add an element at an index out of range" {
                    add(10, 3)
                }
                "add an element at a negative index" {
                    add(-10, 3)
                }
                "add an element at the head" {
                    add(0, 5)
                }
                "remove an element at a specified index" {
                    removeAt(4)
                }
                "remove at an index out of range" {
                    removeAt(100)
                }
                "remove at a negative index" {
                    removeAt(-1)
                }
                "remove an element" {
                    remove(5)
                }
                "remove an element not present in the list" {
                    remove(101)
                }
                "add many elements" {
                    addAll(listOf(5, 6, 7))
                }
                "add many elements at a specified index" {
                    addAll(4, listOf(8, 9, 10))
                }
                "add many elements at negative index" {
                    addAll(-1, listOf(1, 2, 3))
                }
                "add many elements at an index out of range" {
                    addAll(100, listOf(5, 6, 7))
                }
                "remove many elements" {
                    removeAll(listOf(4, 20, 40, 10))
                }
                "remove many elements that were not in the list before" {
                    removeAll(listOf(-101, -102))
                }
                "clearing" {
                    clear()
                }
            }
        }
        describe("map") {
            val list = reactiveList(0, 1, 2)
            val powers = list.map { it * it }
            fun expected() = list.now.map { it * it }
            testListBinding(powers, ::expected) {
                with(list.now) {
                    "add an element" {
                        add(4)
                    }
                    "add an element at the front" {
                        add(0, 5)
                    }
                    "add an element in the middle" {
                        add(2, 7)
                    }
                    "remove an element not present" {
                        remove(-100)
                    }
                    "remove an element" {
                        removeAt(3)
                    }
                    "clear" {
                        clear()
                    }
                    repeat(5) {
                        mutateRandomly(list.now describedAs "the source list", Gen.int(0, 1000))
                    }
                }
            }
        }
        describe("flatMap") {

        }
        xdescribe("iteration") {
            TODO()
        }
        xdescribe("observation") {
            TODO()
        }
    }
})