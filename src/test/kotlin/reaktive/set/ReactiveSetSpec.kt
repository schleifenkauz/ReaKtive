package reaktive.set

import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import reaktive.util.*
import java.util.*

object ReactiveSetSpec : Spek({
    given("a reactive set") {
        describe("mutating") {
            val s = reactiveSet(0, 1, 2, 3)
            val test = mutableSetOf(0, 1, 2, 3)
            testSameEffects(test, s.now) {
                "add a new element" { add(4) }
                "add an element already contained" { add(3) }
                "add a few other elements" { addAll(listOf(1, 4, 5, 9, 10)) }
                "add elements that already are in the set" { addAll(listOf(4, 1)) }
                "add no elements" { addAll(emptyList()) }
                "remove an element" { remove(1) }
                "remove an element not contained" { remove(-4) }
                "remove a few other elements" { removeAll(listOf(-4, -1, 4, 10, 11)) }
                "remove no elements" { removeAll(emptyList()) }
                "remove elements that aren't in the set" { removeAll(listOf(-100)) }
                "clear" { clear() }
            }
        }
        describe("iterating") {
            context("iterating through an empty set") {
                val empty = reactiveSet<Nothing>()
                on("hasNext") {
                    it("should return false") {
                        empty.now.iterator().hasNext() shouldBe `false`
                    }
                }
                on("next") {
                    it("should throw NoSuchElementException") {
                        { empty.now.iterator().next() } shouldBe throwing<NoSuchElementException>()
                    }
                }
            }
            context("iterating through a set of one element") {
                val set = reactiveSet(1, 2, 3)
                val itr = set.now.iterator()
                on("hasNext") {
                    it("should return true") {
                        itr.hasNext() shouldBe `true`
                    }
                }
                on("next") {
                    val el = itr.next()
                    it("should return the first element") {
                        el shouldBe equalTo(1)
                    }
                }
                on("remove") {
                    itr.remove()
                    it("should remove the first element") {
                        set.now shouldBe equalTo(setOf(2, 3))
                    }
                }
            }
            context("concurrent modification") {
                val set = reactiveSet(1, 2, 3)
                val itr = set.now.iterator()
                on("modifying the set after the iterator has been created and then calling next") {
                    set.now.add(4)
                    it("should throw ConcurrentModificationException") {
                        { itr.next(); Unit } shouldBe throwing<ConcurrentModificationException>()
                    }
                }
            }
        }
    }
})