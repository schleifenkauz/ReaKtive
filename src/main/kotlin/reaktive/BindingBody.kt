/**
 * @author Nikolaus Knop
 */

package reaktive

/**
 * Used to setup a Binding
 */
interface BindingBody {
    /**
     * Adds the specified [observer] such that it will be killed when the binding is disposed.
     */
    fun addObserver(observer: Observer)

    /**
     * Kills the specified [observer] that was previously added using [addObserver]
     * and ensures that is will not be killed when this binding is disposed.
     */
    fun kill(observer: Observer)
}