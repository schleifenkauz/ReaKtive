package reaktive.set

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import reaktive.list.reactiveList
import reaktive.set.binding.testSetBinding
import reaktive.value.mocks.TestValueChangeHandler
import reaktive.value.reactiveVariable

object ExtensionsSpec : Spek({
    describe("observeEach") {
        val v1 = reactiveVariable(1)
        val v2 = reactiveVariable(2)
        val v3 = reactiveVariable(3)
        val set = reactiveSet(v1, v2)
        val handler = TestValueChangeHandler<Int>()
        val o = set.observeEach { it.observe(handler) }
        on("invalidating a value inside the set") {
            v1.set(3)
            it("should invoke the handler") {
                handler.testValueChanged(v1, 1, 3)
            }
        }
        on("adding a new variable to the set and changing it") {
            set.now.add(v3)
            v3.set(4)
            it("should invoke the handler") {
                handler.testValueChanged(v3, 3, 4)
            }
        }
        on("removing a variable from the set and changing it") {
            set.now.remove(v2)
            v2.set(5)
            it("should not invoke the handler") {
                handler.testNoChange()
            }
        }
        afterGroup { o.kill() }
    }
    describe("asSet") {
        val l = reactiveList(1, 2, 3)
        testSetBinding(l.asSet(), { l.now.toSet() }) {
            with(l.now) {
                "add an element already existing (3)" { add(0, 3) }
                "add a new element (4)" { add(4, 2) }
                "add another new element (239)" { add(239) }
                "set the element at index 2 to 10" { set(2, 10) }
                "remove an existing element that has duplicates (3)" { remove(3) }
                "remove an non-existing element(7)"{ remove(7) }
                "remove an existing element without duplicates(3)"{ remove(3) }
                "clear" { clear() }
            }
        }
    }
})