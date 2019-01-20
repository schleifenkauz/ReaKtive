/**
 *@author Nikolaus Knop
 */

package reaktive.value.binding

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import reaktive.value.now
import reaktive.value.reactiveVariable

internal object UtilityBindingsSpec : Spek({
    describe("orElse") {
        val source = reactiveVariable<Int?>(1)
        val otherwise = reactiveVariable(2)
        testBinding(source.orElse(otherwise), { -> source.now ?: otherwise.now }) {
            "set source to null" {
                source.set(null)
            }
            "set alternative to 3" {
                otherwise.set(3)
            }
            "set source to 2" {
                source.set(2)
            }
        }
    }
    describe("takeIf binding") {
        val source = reactiveVariable(2)
        val binding = source.takeIf { it % 2 == 0 }
        fun expected() = source.now.takeIf { it % 2 == 0 }
        testBinding(binding, ::expected) {
            with(source) {
                "set to even" {
                    set(4)
                }
                "set to odd" {
                    set(3)
                }
                "set to even" {
                    set(4)
                }
            }
        }
    }
})