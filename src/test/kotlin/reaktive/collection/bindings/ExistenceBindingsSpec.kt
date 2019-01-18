package reaktive.collection.bindings

import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import reaktive.collection.binding.*
import reaktive.help.`false`
import reaktive.help.`true`
import reaktive.set.reactiveSet
import reaktive.value.binding.map
import reaktive.value.binding.testBinding
import reaktive.value.help.value
import reaktive.value.now
import reaktive.value.reactiveVariable

internal object ExistenceBindingsSpec : Spek({
    describe("existence bindings") {
        describe("all binding") {
            val first = reactiveVariable(1)
            val second = reactiveVariable(2)
            val third = reactiveVariable(3)
            val s = reactiveSet(
                first,
                second,
                third
            )
            val b = s.allR { v -> v.map { n -> n % 2 == 0 } }
            it("should initially be false") {
                b shouldMatch value(`false`)
            }
            on("setting both odd values to be even") {
                first.set(0)
                third.set(4)
                it("should be true") {
                    b shouldMatch value(`true`)
                }
            }
            on("setting the even value to be odd") {
                second.set(1)
                it("should be false") {
                    b shouldMatch value(`false`)
                }
            }
            on("removing the odd value") {
                s.now.remove(second)
                it("should be true") {
                    b shouldMatch value(`true`)
                }
            }
            on("adding an odd value") {
                s.now.add(reactiveVariable(23))
                it("should be false") {
                    b shouldMatch value(`false`)
                }
            }
        }
        describe("count binding") {
            val set = reactiveSet(1, 2, 3, 4)
            val isEven = { x: Int -> x % 2 == 0 }
            val countEven = set.count(isEven)
            fun expected() = set.now.count(isEven)
            testBinding(countEven, ::expected) {
                with(set.now) {
                    "add even element" { add(6) }
                    "remove even element" { remove(4) }
                    "add odd element" { add(5) }
                    "remove odd element" { remove(3) }
                }
            }
        }
        describe("contains binding") {
            context("initially contains it") {
                val set = reactiveSet(1, 2, 3)
                val searched = reactiveVariable(4)
                val containsSearched = set.contains(searched)
                fun expected() = set.now.contains(searched.now)

            }
            context("initially doesn't contain it") {

            }
        }
    }
})