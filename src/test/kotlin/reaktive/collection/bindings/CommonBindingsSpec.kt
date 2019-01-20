package reaktive.collection.bindings

import com.natpryce.hamkrest.should.describedAs
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import reaktive.collection.MutableReactiveCollection
import reaktive.collection.binding.size
import reaktive.random.Gen
import reaktive.set.reactiveSet
import reaktive.value.binding.testBinding

internal object CommonBindingsSpec : Spek({
    val collection: MutableReactiveCollection<Int> = reactiveSet()
    val size = collection.size
    fun expected() = collection.now.size
    describe("size binding") {
        testBinding(size, ::expected) {
            with(collection.now) {
                "add an element" { add(1) }
                "remove an element" { remove(1) }
                "add 5 elements" { addAll(setOf(1, 2, 3, 4, 5)) }
                "add 1 new element" { addAll(setOf(2, 4, 8)) }
                "remove 3 elements" { removeAll(setOf(234, 34, 234, 3, 5, 1)) }
                repeat(10) {
                    mutateRandomly(collection.now describedAs "the source", Gen.int(0, 1000))
                }
                "clear the set" { clear() }
            }
        }
    }
})