/**
 * @author Nikolaus Knop
 */

package reaktive.list

import reaktive.list.impl.ReactiveListImpl

fun <E> reactiveList(vararg elements: E): MutableReactiveList<E> = mutableListOf(*elements).reactive()

fun <E> Iterable<E>.toReactiveList(): MutableReactiveList<E> = toMutableList().reactive()

fun <E> MutableList<E>.reactive(): MutableReactiveList<E> = ReactiveListImpl(this)

fun <E> List<E>.reactive(): ReactiveList<E> = toMutableList().reactive()

fun <E> unmodifiableReactiveList(vararg elements: E): ReactiveList<E> = unmodifiableReactiveList(elements.asIterable())

fun <E> unmodifiableReactiveList(elements: Iterable<E>): ReactiveList<E> = TODO()