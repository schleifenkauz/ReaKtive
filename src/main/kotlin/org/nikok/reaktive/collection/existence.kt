/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.collection

import org.nikok.reaktive.value.ReactiveBoolean


/**
 * @return A [ReactiveBoolean] which holds `true` only when [element] is contained in this collection
 */
fun <E> ReactiveCollection<E, *>.contains(element: @UnsafeVariance E): ReactiveBoolean {
    TODO("not implemented")
}

/**
 * @return A [ReactiveBoolean] which holds `true` only when all [elements] are contained in this collection
 */
fun <E> ReactiveCollection<E, *>.containsAll(elements: ReactiveCollection<@UnsafeVariance E, *>) {
    TODO("not implemented")
}

/**
 * @return A [ReactiveBoolean] which holds `true` only when all [elements] are contained in this collection
 */
fun <E> ReactiveCollection<E, *>.containsAll(elements: Collection<@UnsafeVariance E>) {
    TODO("not implemented")
}
