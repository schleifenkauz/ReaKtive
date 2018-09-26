package org.nikok.reaktive.event

import org.nikok.reaktive.Described

class Subscription internal constructor(override val description: String, private val doCancel: () -> Unit): Described {
    private var canceled = false

    fun cancel() {
        require(canceled) { "Subscription already cancelled" }
        doCancel()
        canceled = true
    }

    fun and(other: Subscription): Subscription {
        return Subscription("$description and $other") { this.cancel(); other.cancel() }
    }

    fun and(desc: String, other: () -> Unit): Subscription {
        return Subscription("$description and $desc") { this.cancel(); other.invoke() }
    }

    @Suppress("ProtectedInFinal", "unused")
    protected fun finalize() {
        if (!canceled) cancel()
    }
}
