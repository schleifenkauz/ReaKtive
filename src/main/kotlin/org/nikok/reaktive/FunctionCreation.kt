package org.nikok.reaktive

import kotlin.reflect.KFunction1

/**
 * @author Nikolaus Knop
 */

/**
 * @return an [InvalidationHandler] described by [desc] which uses [handle] to handle invalidation
 */
fun <T> invalidationHandler(desc: String, handle: (Reactive) -> T): InvalidationHandler = handle.withDescription(desc)

/**
 * @return a [InvalidationHandler] using the name of the reflected function
 * @throws IllegalArgumentException if the receiver is an anonymous function
 */
fun <T> (KFunction1<Reactive, T>).asInvalidationHandler(): InvalidationHandler {
    return invalidationHandler(name, this)
}

/**
 * @return an [InvalidationHandler] described by [desc] which uses the receiver function to handle invalidation
 */
infix fun <T> ((Reactive) -> T).withDescription(desc: String): InvalidationHandler {
    return object : InvalidationHandler {
        override fun invalidated(invalidated: Reactive) {
            invoke(invalidated)
        }

        override val description: String = desc

        override fun toString() = description
    }
}
