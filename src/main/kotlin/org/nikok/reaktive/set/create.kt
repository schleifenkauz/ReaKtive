/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.set

/**
 * @return a [ReactiveSet] containing [elements]
 */
fun <E> reactiveSetOf(vararg elements: E): ReactiveSet<E> = TODO()

/**
 * @return a [ReactiveSet] consisting of the elements in the receiver
 */
fun <E> Iterable<E>.toReactiveSet(): ReactiveSet<E> = TODO()