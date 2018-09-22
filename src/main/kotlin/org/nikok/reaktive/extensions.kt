/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive

/**
 * Observe this [Reactive] with a [InvalidationHandler] with description [handlerName] calling [handler] when invalidated
 */
fun <T> Reactive.observe(handlerName: String, handler: (Reactive) -> T): Observer =
        observe(invalidationHandler(handlerName, handler))

/**
 * Syntactic sugar for `observe(handlerName) { _ -> handler() }`
 */
fun <T> Reactive.observe(handlerName: String, handler: () -> T): Observer = observe(handlerName) { _ -> handler() }