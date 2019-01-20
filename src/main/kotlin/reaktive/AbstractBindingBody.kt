/**
 *@author Nikolaus Knop
 */

package reaktive

import java.util.*

internal abstract class AbstractBindingBody: BindingBody {
    private val observers = LinkedList<Observer>()

    final override fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    fun dispose() {
        observers.forEach { it.tryKill() }
    }
}