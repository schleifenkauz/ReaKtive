package reaktive.collection.bindings

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.collection.MutableReactiveCollection
import reaktive.collection.binding.size
import reaktive.random.Gen
import reaktive.set.reactiveSet
import reaktive.value.binding.testBinding

internal object CommonBindingsSpec : Spek({
    val collection: MutableReactiveCollection<Int> = reactiveSet()
    val size = collection.size
    fun expected() = collection.now.size
    Feature("common bindings") {
        Scenario("size binding on reactive set") {
            testBinding(size, ::expected) {
                with(collection.now) {
                    "add an element" { add(1) }
                    "remove an element" { remove(1) }
                    "add 5 elements" { addAll(setOf(1, 2, 3, 4, 5)) }
                    "add 1 new element" { addAll(setOf(2, 4, 8)) }
                    "remove 3 elements" { removeAll(setOf(234, 34, 234, 3, 5, 1)) }
                    repeat(10) {
                        mutateRandomly("the collection", collection.now, Gen.int(0, 1000))
                    }
                    "clear the set" { clear() }
                }
            }
        }
    }
})