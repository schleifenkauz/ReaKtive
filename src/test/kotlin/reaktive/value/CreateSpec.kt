package reaktive.value

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import reaktive.value.help.value

internal object CreateSpec : Spek({
    fun testInitialValue(createValue: (Int) -> Value<Int>) {
        val v = createValue(1)
        v shouldMatch value(equalTo(1))
    }

    describe("value(name, value)") {
        it("should return a Value of value described by name") {
            testInitialValue(::value)
        }
    }
    describe("variable(name, value)") {
        it("should return a Variable of value described by name") {
            testInitialValue(::variable)
        }
    }
    describe("reactiveValue(name, value)") {
        it("should return a ReactiveValue of value described by name") {
            testInitialValue(::reactiveValue)
        }
    }
    describe("reactiveVariable(name, value)") {
        it("should return a ReactiveVariable of value described by name") {
            testInitialValue(::reactiveVariable)
        }
    }
})