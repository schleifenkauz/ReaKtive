/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.list

fun <E> reactiveList(name: String, vararg elements: E): MutableReactiveList<E> = TODO()

fun <E> Iterable<E>.toReactiveList(name: String): MutableReactiveList<E> = TODO()

fun <E> MutableList<E>.reactive(name: String): MutableReactiveList<E> = TODO()

fun <E> List<E>.reactive(name: String): ReactiveList<E> = TODO()

fun <E> unmodifiableReactiveList(name: String, vararg elements: E): ReactiveList<E> = TODO()