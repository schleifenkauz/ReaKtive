/**
 *@author Nikolaus Knop
 */

package reaktive.list

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.list.binding.values
import reaktive.value.binding.testBinding
import reaktive.value.now
import reaktive.value.reactiveVariable

object ListBindingsSpec : Spek({
    Feature("list bindings"){
        Scenario("values binding") {
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
            }
        }
        Scenario("get binding") {
            val l = reactiveList(0, 1, 2)
            val i = reactiveVariable(0)
            val b = l[i]
            testBinding(b, { l.now.getOrNull(i.now) }) {
                "clear list" {
                    l.now.clear()
                }
                "add items to list" {
                    l.now.addAll(listOf(3, 4, 5))
                }
                "set index to 2" {
                    i.set(2)
                }
                "remove last item" {
                    l.now.removeAt(2)
                }
                "set index to negative value" {
                    i.set(-1)
                }
            }
        }
    }
})