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

fun <R : Reactive, F> R.map(f: (R) -> F): Binding<F> = binding<F>(dependencies(this)) { f(this) }

fun Iterable<Observer>.combined() = Observer { forEach { it.kill() } }