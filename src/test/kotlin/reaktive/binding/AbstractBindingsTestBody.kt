/**
 *@author Nikolaus Knop
 */

package reaktive.binding

import com.natpryce.hamkrest.Described
import org.jetbrains.spek.api.dsl.*
import reaktive.random.Gen

internal abstract class AbstractBindingsTestBody(private val spec: SpecBody) {
    @PublishedApi internal abstract fun ActionBody.check()

    inline operator fun String.invoke(crossinline action: () -> Unit) {
        spec.on(this) {
            action()
            check()
        }
    }

    fun <T> mutateRandomly(collection: Described<out MutableCollection<T>>, generator: Gen<T>) {
        val col = collection.value
        val desc = collection.description
        val addOrRemove = Gen.int(0, 2).next()
        val element = generator.next()
        if (addOrRemove == 0) {
            "add $element to $desc" {
                col.add(element)
            }
        } else {
            "remove $element from $desc" {
                col.remove(element)
            }
        }
    }
}