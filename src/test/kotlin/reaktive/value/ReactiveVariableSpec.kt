/**
 *@author Nikolaus Knop
 */

package reaktive.value

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.jetbrains.spek.api.include
import reaktive.Observer
import reaktive.binding.help.testBinding
import reaktive.value.mocks.TestValueChangeHandler

internal object ReactiveVariableSpec : Spek({
    given("a reactive variable with initial value 1") {
        include(VariableSpec.common(reactiveVariable(1)))
        val v = reactiveVariable(1)
        val handler = TestValueChangeHandler<Int>()
        lateinit var obs: Observer
        on("observing the variable and then setting it") {
            obs = v.observe(handler)
            v.set(3)
            it("should invoke the handler") {
                handler.testValueChanged(v, 1, 3)
            }
        }
        on("setting the value to the old value") {
            v.set(v.get())
            it("should not invoke the handler") {
                handler.testNoChange()
            }
        }
        on("stopping the observation and then setting the value") {
            obs.kill()
            v.set(4)
            it("should not invoke the handler") {
                handler.testNoChange()
            }
        }
        describe("map") {
            val square = v.map { it * it }
            fun expected() = v.now * v.now
            testBinding(square, ::expected) {
                set(v) {
                    to(3)
                    to(-6)
                    to(0)
                }
            }
        }
        describe("flatMap") {
            val another = reactiveVariable(3)
            val product = v.flatMap { vNow ->
                another.map { anotherNow -> anotherNow * vNow }
            }

            fun expected() = another.now * v.now
            testBinding(product, ::expected) {
                test("set another to 34") { another.set(34) }
                test("set v to 3") { v.set(3) }
                test("set another to -5") { another.set(-5) }
            }
        }
    }
})