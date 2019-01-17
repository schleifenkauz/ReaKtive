package reaktive.collection.bindings

import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import reaktive.collection.binding.all
import reaktive.help.`false`
import reaktive.help.`true`
import reaktive.set.reactiveSet
import reaktive.value.help.value
import reaktive.value.reactiveVariable

internal object ExistenceBindingsSpec : Spek({
    given("a reactive set and an 'all-binding'") {
        val first = reactiveVariable(1)
        val second = reactiveVariable(2)
        val third = reactiveVariable(3)
        val s = reactiveSet(
            first,
            second,
            third
        )
        val b = s.all { v -> v.map { n -> n % 2 == 0 } }
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
})