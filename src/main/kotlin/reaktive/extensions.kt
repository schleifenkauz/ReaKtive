/**
 * @author Nikolaus Knop
 */

package reaktive

/**
 * Syntactic sugar for observe { -> handler() }
 */
@Suppress("RedundantLambdaArrow") inline fun Reactive.observe(crossinline handler: () -> Unit) = observe { handler() }