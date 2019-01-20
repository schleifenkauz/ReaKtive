/**
 * @author Nikolaus Knop
 */

package reaktive.set

import reaktive.set.impl.MutableReactiveSetImpl
import reaktive.set.impl.UnmodifiableReactiveSet

/**
 * @return a [ReactiveSet] containing [elements]
 */
fun <E> reactiveSet(vararg elements: E): MutableReactiveSet<E> =
        MutableReactiveSetImpl(elements.toMutableSet())

/**
 * @return a [ReactiveSet] consisting of the elements in the receiver
 */
fun <E> Iterable<E>.toReactiveSet(): MutableReactiveSet<E> =
        MutableReactiveSetImpl(toMutableSet())

/**
 * @return a [MutableReactiveSet] wrapping the [MutableSet]
 */
fun <E> MutableSet<E>.reactive(): MutableReactiveSet<E> = MutableReactiveSetImpl(this)

/**
 * @return an unmodifiable set containing the specified [elements]
 */
fun <E> unmodifiableReactiveSet(elements: Iterable<E>): ReactiveSet<E> =
        UnmodifiableReactiveSet(elements)
