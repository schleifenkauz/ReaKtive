package reaktive.collection.bindings

import org.jetbrains.spek.api.Spek
import reaktive.binding.help.testBinding
import reaktive.collection.MutableReactiveCollection
import reaktive.collection.size
import reaktive.set.reactiveSet

internal object ExistenceBindingsSpec: Spek({
    val collection: MutableReactiveCollection<Int> = reactiveSet()
    val size = collection.size
    fun expected() = collection.now.size
    testBinding(size, ::expected) {
        collection.now.add(1)
        collection.now.remove(1)
        collection.now.addAll(setOf(1, 2, 3, 4, 5))
        collection.now.addAll(setOf(2, 4, 8))
        collection.now.removeAll(setOf(234, 34, 234, 3, 5, 1))
        collection.now.clear()
    }
})