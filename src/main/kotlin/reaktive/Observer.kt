/**
 *@author Nikolaus Knop
 */

package reaktive

/**
 * Result of observing a [Reactive]
 */
class Observer internal constructor(private val doKill: () -> Unit) {
    private var killed = false

    /**
     * Try to kill
     */
    @Suppress("unused", "ProtectedInFinal") protected fun finalize() {
        tryKill()
    }

    /**
     * Assuming `o` is a [Reactive] and `observer`
     * was returned by `o.observe {  }` than o.kill() stops observing `o`
     * @throws IllegalStateException when called twice
     */
    fun kill() {
        check(!killed) { "Cannot kill $this when already killed" }
        doKill()
        killed = true
    }

    /**
     * Tries to [kill] this [Observer]
     */
    fun tryKill() {
        if (!killed) kill()
    }

    internal fun and(otherKill: () -> Unit): Observer =
        Observer { kill(); otherKill.invoke() }

    infix fun and(other: Observer): Observer {
        return this.and { other.kill() }
    }

    companion object {
        /**
         * An [Observer] that does nothing on being killed
         */
        val nothing: Observer get() = Observer { }
    }
}