package org.nikok.reaktive.event

import org.nikok.reaktive.Described

/**
 * A subscription to an [EventStream]
*/
class Subscription internal constructor(override val description: String, private val doCancel: () -> Unit): Described {
    private var canceled = false

    /**
     * Cancel the subscription which removes the event handler from the associated [EventStream]
     * @throws IllegalStateException when this subscription is already cancelled
    */
    fun cancel() {
        check(!canceled) { "Subscription already cancelled" }
        doCancel()
        canceled = true
    }

    /**
     * Return a [Subscription] which when cancelled cancels both this and the [other] [Subscription]
    */
    fun and(other: Subscription): Subscription {
        return Subscription("$description and $other") { this.cancel(); other.cancel() }
    }

    /**
    * Return a [Subscription] which when cancelled cancels both this [Subscription] and invokes [other]
    */
    fun and(desc: String, other: () -> Unit): Subscription {
        return Subscription("$description and $desc") { this.cancel(); other.invoke() }
    }

    /**
     * Cancel this subscription
    */
    @Suppress("ProtectedInFinal", "unused")
    protected fun finalize() {
        if (!canceled) cancel()
    }
}
