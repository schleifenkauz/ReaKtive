/**
 *@author Nikolaus Knop
 */

package reaktive.list

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import reaktive.list.binding.values
import reaktive.random.Gen
import reaktive.value.now
import reaktive.value.reactiveVariable

object BindingsSpec : Spek({
    describe("values binding") {
        val v1 = reactiveVariable(1)
        val v2 = reactiveVariable(2)
        val v3 = reactiveVariable(3)
        val source = reactiveList(v1, v2, v3)
        val values = source.values()
        fun expected() = source.now.map { it.now }
        testListBinding(values, ::expected) {
            "modify a variable" {
                v1.set(4)
            }
            "remove a variable" {
                source.now.remove(v2)
            }
            "then modify the removed variable" {
                v2.set(5)
            }
            "add it again" {
                source.now.add(v2)
            }
            "modify it again" {
                v2.set(7)
            }
            val v4 = reactiveVariable(10)
            "add a completely new variable" {
                source.now.add(v4)
            }
            "then modify the new variable" {
                v4.set(11)
            }
            "remove it again" {
                source.now.remove(v4)
            }
            "and then modify it" {
                v4.set(12)
            }
            val toModify = Gen.choose(v1, v2, v3, v4)
            val value = Gen.int(-1000, 1000)
            repeat(5) {
                "modify variable randomly" {
                    toModify.next().set(value.next())
                }
            }
        }
    }
})