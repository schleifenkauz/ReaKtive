package org.nikok.reaktive.mocks

internal object TestKill : () -> Unit {
    var killed = false;
        private set
        get() {
            val wasKilled = field
            reset()
            return wasKilled
        }

    fun reset() {
        killed = false
    }

    override fun invoke() {
        killed = true
    }
}