/**
 * @author Nikolaus Knop
 */

package reaktive.collection.binding

import reaktive.collection.ReactiveCollection
import reaktive.value.binding.Binding
import reaktive.value.binding.createBinding

@PublishedApi internal inline fun <E, T> ReactiveCollection<E>.sumBy(
    neutral: T,
    crossinline minus: (T, T) -> T,
    crossinline plus: (T, T) -> T,
    crossinline selector: (E) -> T
): Binding<T> =
    createBinding(neutral) {
        for (e in now) {
            withValue { set(plus(it, selector(e))) }
        }
        val obs = observeCollection { ch ->
            if (ch.wasAdded) withValue { set(plus(it, selector(ch.added))) }
            if (ch.wasRemoved) withValue { set(minus(it, selector(ch.removed))) }
        }
        addObserver(obs)
    }

inline fun <E> ReactiveCollection<E>.sumBy(crossinline selector: (E) -> Int) =
    sumBy(0, Int::minus, Int::plus, selector)

@JvmName("sumOfInt")
fun ReactiveCollection<Int>.sum() = sumBy { it }

inline fun <E> ReactiveCollection<E>.sumByDouble(crossinline selector: (E) -> Double): Binding<Double> =
    sumBy(0.0, Double::minus, Double::plus, selector)

@JvmName("sumOfDouble")
fun ReactiveCollection<Double>.sum() = sumByDouble { it }

