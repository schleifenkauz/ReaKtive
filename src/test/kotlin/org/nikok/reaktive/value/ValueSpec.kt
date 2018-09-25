package org.nikok.reaktive.value

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.describedAs
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.jetbrains.spek.api.include

internal object ValueSpec : Spek({
    given("a constant value 1") {
        val v = value("subject", 1)
        include(ValueSpec.common(v))
    }
}) {
    fun common(v: Value<Int>): Spek {
        return Spek.wrap {
            on("converting the variable to a string") {
                val str = v.toString()
                it("should return the description followed by colon and the value") {
                    val value = v.get()
                    val desc = v.description
                    val expected = "$desc: $value"
                    str describedAs "the string representation" shouldMatch equalTo(expected)
                }
            }
        }
    }
}