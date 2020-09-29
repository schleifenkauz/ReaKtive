/**
 * @author Nikolaus Knop
 */

package reaktive

import reaktive.value.binding.Binding
import reaktive.value.binding.binding

/**
 * Syntactic sugar for observe { -> handler() }
 */
inline fun Reactive.observe(crossinline handler: () -> Unit) = observe { handler() }

fun <R : Reactive> R.asValue(): Binding<R> = binding<R>(dependencies(this)) { this }

fun Iterable<Observer>.combined() = Observer { forEach { it.kill() } }