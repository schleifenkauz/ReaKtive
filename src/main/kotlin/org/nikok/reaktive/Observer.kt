/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive

/**
 * Result of observing an [Reactive]
 */
class Observer internal constructor(
    override val description: String, private val doKill: () -> Unit
) : Described {
    private var killed = false

    @Suppress("unused", "ProtectedInFinal") protected fun finalize() {
        tryKill()
    }

    override fun toString(): String {
        return "Observer: $description"
    }

    /**
     * Assuming that `o` is an [Reactive] and `observer`
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

    internal fun and(description: String, otherKill: () -> Unit): Observer =
            Observer("$this and $description") { kill(); otherKill.invoke() }

    internal infix fun and(other: Observer): Observer {
        return this.and(other.description) { other.kill() }
    }

    companion object {
        /**
         * An [Observer] that does nothing on being killed
         */
        val nothing: Observer get() = Observer("nothing") { }
    }
}