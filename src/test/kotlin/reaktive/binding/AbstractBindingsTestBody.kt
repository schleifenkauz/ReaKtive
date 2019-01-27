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

    fun <T> mutateRandomly(collection: Described<out MutableCollection<in T>>, generator: Gen<T>) {
        val col = collection.value
        val desc = collection.description
        val addOrRemove = Gen.int(0, 2).next()
        if (addOrRemove == 0) {
            val element = generator.next()
            "add $element to $desc" {
                col.add(element)
            }
        } else {
            val element = Gen.fromList(col.toList())
            "remove $element from $desc" {
                col.remove(element)
            }
        }
    }

    fun <T> mutateListRandomly(list: Described<out MutableList<in T>>, generator: Gen<T>) {
        val l = list.value
        val desc = list.description
        val addOrRemove = Gen.int(0, 4).next()
        when (addOrRemove) {
            0 -> {
                val element = generator.next()
                val index = Gen.index(l).next()
                "add $element to $desc" {
                    l.add(index, element)
                }
            }
            1 -> {
                if (l.isNotEmpty()) {
                    val element = Gen.fromList(l)
                    "remove $element from $desc" {
                        l.remove(element)
                    }
                }
            }
        }
    }
}