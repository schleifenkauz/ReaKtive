/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive

abstract class AbstractDisposable : Disposable {
    /**
     * Dispose this object
     */
    protected abstract fun doDispose()

    private var disposed = false

    final override fun dispose() {
        require(!disposed) { "$this already exposed" }
        doDispose()
        disposed = true
    }
}