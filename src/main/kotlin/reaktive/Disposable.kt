/**
 * @author Nikolaus Knop
 */

package reaktive

/**
 * An object that can be disposed to cleanup any resources associated with this object
*/
interface Disposable {
    /**
     * Disposes this object
     * @throws IllegalStateException if this object was disposed before
    */
    fun dispose()
}