/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.collection

import org.nikok.reaktive.Described

/**
 * A function object that is used to observe [ReactiveCollection]s
*/
interface CollectionChangeHandler<in E, in Ch: CollectionChange<@UnsafeVariance E>>: Described {
    /**
     * Called when [change] occurred
    */
    fun handle(change: Ch)
}