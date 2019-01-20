/**
 *@author Nikolaus Knop
 */

package reaktive

/**
 * Skeletal implementation of [Disposable]
 * @constructor
*/
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