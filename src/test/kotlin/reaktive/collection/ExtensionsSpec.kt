package reaktive.collection

import org.jetbrains.spek.api.Spek
import reaktive.set.reactiveSet
import reaktive.value.binding.testBinding

internal object ExtensionsSpec : Spek({
    val collection: MutableReactiveCollection<Int> = reactiveSet()
    val size = collection.size
    fun expected() = collection.now.size
    testBinding(size, ::expected) {
        with(collection.now) {
            "add an element" { add(1) }
            "remove an element" { remove(1) }
            "add 5 elements" { addAll(setOf(1, 2, 3, 4, 5)) }
            "add 1 new element" { addAll(setOf(2, 4, 8)) }
            "remove 3 elements" { removeAll(setOf(234, 34, 234, 3, 5, 1)) }
            "clear the set" { clear() }
        }
    }
})