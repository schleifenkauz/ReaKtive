/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.value.binding

import org.nikok.reaktive.Disposable
import org.nikok.reaktive.Reactive
import org.nikok.reaktive.value.ReactiveValue

/**
 * A Binding is a [ReactiveValue] which is in a relation to other [Reactive]s and is always updated accordingly
 */
interface Binding<out T> : ReactiveValue<T>, Disposable {
    /**
     * Stops this [Binding] from being updated meaning that it is useless and from then stays constant
     * * Implementing classes **must** call this method in `finalize`
     */
    override fun dispose()
}