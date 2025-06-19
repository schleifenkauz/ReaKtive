/**
 *@author Nikolaus Knop
 */

package reaktive.value.base

import reaktive.Observer
import reaktive.and
import reaktive.getValue
import reaktive.value.*
import reaktive.value.impl.VariableSetterImpl
import java.lang.ref.WeakReference

/**
 * Abstract base class for [Variable]s implementing
 * * Binding
 * * setter
 * @constructor
 * @return a new [AbstractVariable]
 */
abstract class AbstractVariable<T> : Variable<T>, AbstractValue<T>() {
    /**
     * Actually set the value of this [Variable] to [value]
     */
    protected abstract fun doSet(value: T)

    override fun set(value: T) {
        if (isBound && !updating) throw AlreadyBoundException.attemptedSetTo(this, value)
        doSet(value)
    }

    private var updating = false

    private inline fun updating(block: () -> Unit) {
        updating = true
        block()
        updating = false
    }

    final override fun bind(other: ReactiveValue<T>): Observer {
        if (isBound) throw AlreadyBoundException.attemptedBindTo(this, other)
        require(other != this) { "Cannot bind $this to itself" }
        val setter = setter
        setter.set(other.get())
        val bindingObserver = other.observe { new: T -> updating { setter.set(new) } }
        isBound = true
        return bindingObserver and BindObserver(WeakReference(this))
    }

    final override var isBound: Boolean = false; protected set

    override val setter: VariableSetter<T> = VariableSetterImpl(this)

    private class BindObserver(private val variable: WeakReference<AbstractVariable<*>>): Observer() {
        override fun doKill() {
            variable.get()?.isBound = false
        }
    }
}