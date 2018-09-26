/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.set

import org.nikok.reaktive.set.impl.UnmodifiableReactiveSet

/**
 * @return a [ReactiveSet] containing [elements]
 */
fun <E> reactiveSet(description: String, vararg elements: E): MutableReactiveSet<E> = TODO()

/**
 * @return a [ReactiveSet] consisting of the elements in the receiver
 */
fun <E> Iterable<E>.toReactiveSet(description: String): MutableReactiveSet<E> = TODO()

/**
 * @return a [MutableReactiveSet] wrapping the [MutableSet]
 */
fun <E> MutableSet<E>.reactive(): MutableReactiveSet<E> = TODO()

/**
 * @return an unmodifiable set containing the specified [elements]
 */
fun <E> unmodifiableReactiveSet(description: String, elements: Iterable<E>): ReactiveSet<E> =
        UnmodifiableReactiveSet(description, elements)
