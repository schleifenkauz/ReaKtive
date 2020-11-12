/**
 *@author Nikolaus Knop
 */

package reaktive

internal abstract class AbstractBindingBody : BindingBody {
    private val observers = mutableSetOf<Observer>()

    final override fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    override fun kill(observer: Observer) {
        check(observers.remove(observer)) { "The observer was not added before" }
        observer.kill()
    }

    fun dispose() {
        observers.forEach { it.tryKill() }
    }
}