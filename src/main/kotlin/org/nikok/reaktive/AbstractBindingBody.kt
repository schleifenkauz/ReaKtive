/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive

import java.util.*

internal abstract class AbstractBindingBody: BindingBody {
    private val observers = LinkedList<Observer>()

    final override fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    fun dispose() {
        observers.forEach(Observer::kill)
    }
}