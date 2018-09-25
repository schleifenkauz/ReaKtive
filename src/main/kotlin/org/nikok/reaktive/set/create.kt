/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.set

/**
 * @return a [ReactiveSet] containing [elements]
 */
fun <E> reactiveSetOf(vararg elements: E): MutableReactiveSet<E> = TODO()

/**
 * @return a [ReactiveSet] consisting of the elements in the receiver
 */
fun <E> Iterable<E>.toReactiveSet(): MutableReactiveSet<E> = TODO()

/**
 * @return a [MutableReactiveSet] wrapping the [MutableSet]
*/
fun <E> MutableSet<E>.reactive(): MutableReactiveSet<E> = TODO()