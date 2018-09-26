/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive

/**
 * Used to setup a Binding
*/
interface BindingBody {
    /**
     * Adds the specified [observer] such that it will be killed when the binding is finalized
    */
    fun addObserver(observer: Observer)
}