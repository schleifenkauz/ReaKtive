/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.value

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import com.natpryce.hamkrest.throws
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.jetbrains.spek.api.include
import org.nikok.reaktive.Observer
import org.nikok.reaktive.help.*
import org.nikok.reaktive.value.help.value

internal object VariableSpec : Spek({
    given("a variable with initial value of 1") {
        val v = variable("subject", 1)
        include(VariableSpec.common(v))
    }
}) {
    fun common(v: Variable<Int>): Spek = Spek.wrap {
        include(ValueSpec.common(variable("subject", 1)))
        on("setting it to 2") {
            v.set(2)
            it("should return 2 on getting the value") {
                v shouldMatch value(equalTo(2))
            }
        }
        val rv = reactiveVariable("binding variable", 3)
        lateinit var obs: Observer
        on("binding it to a reactive value with value 3") {
            obs = v.bind(rv)
            it("should set the value to 3") {
                v shouldMatch value(equalTo(3))
            }
            it("should be bound") {
                v.isBound shouldBe `true`
            }
        }
        on("then setting the bound reactive value to 4") {
            rv.set(4)
            it("should set the value of the variable to 4") {
                v shouldMatch value(equalTo(4))
            }
        }
        on("killing the observer and then setting the reactive value to 5") {
            obs.kill()
            rv.set(5)
            it("should not update the variable") {
                v shouldMatch value(equalTo(4))
            }
            it("should not be bound") {
                v.isBound shouldMatch `false`
            }
        }
        on("binding the variable and then setting it") {
            val obs2 = v.bind(reactiveValue("binding value", 5))
            it("should throw an AlreadyBoundException") {
                { v.set(1209) } shouldMatch throws<AlreadyBoundException>()
                obs2.kill()
            }
        }
        on("binding the variable and again binding it") {
            val obs3 = v.bind(reactiveValue("binding value", 6))
            it("should throw an AlreadyBoundException") {
                val rv2 = reactiveValue("nope", 7);
                { v.bind(rv2); Unit } shouldMatch throws<AlreadyBoundException>()
                obs3.kill()
            }
        }
    }
}