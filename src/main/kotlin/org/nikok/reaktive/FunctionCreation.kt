package org.nikok.reaktive

/**
 * @author Nikolaus Knop
 */

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
