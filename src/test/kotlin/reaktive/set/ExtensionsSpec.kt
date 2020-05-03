package reaktive.set

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import reaktive.value.mocks.TestValueChangeHandler
import reaktive.value.reactiveVariable

object ExtensionsSpec: Spek({
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
        on("removing a variable from the set and changing it"){
            set.now.remove(v2)
            v2.set(5)
            it("should not invoke the handler") {
                handler.testNoChange()
            }
        }
        afterGroup { o.kill() }
    }
})