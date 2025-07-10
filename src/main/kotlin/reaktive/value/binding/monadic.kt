package reaktive.value.binding

import reaktive.Observer
import reaktive.dependencies
import reaktive.value.ReactiveValue
import reaktive.value.impl.ConstantReactiveValue
import reaktive.value.now

/**
 * @return a [Binding] which always holds the value of [f] applied to the value of this [ReactiveValue]
 * * [map] makes [ReactiveValue] an instance of functor
 */
inline fun <T, F> ReactiveValue<T>.map(crossinline f: (T) -> F): Binding<F> =
    if (this is ConstantReactiveValue) constantBinding(f(now))
    else binding<F>(dependencies(this)) { f(this.get()) }

/**
 * @return a [Binding] which is always bound to the value of [f] applied to the value of this [ReactiveValue]
 * * [flatMap] makes [ReactiveValue] an instance of Monad
 */
inline fun <T, F> ReactiveValue<T>.flatMap(crossinline f: (T) -> ReactiveValue<F>): Binding<F> {
    if (this is ConstantReactiveValue) return f(now).asBinding()
    val first = f(this.now)
    return createBinding(first.now) {
        var oldBindObserver: Observer = bind(first)
        addObserver(oldBindObserver)
        val obs = observe { _, _, new ->
            kill(oldBindObserver)
            oldBindObserver = bind(f(new))
            addObserver(oldBindObserver)
        }
        addObserver(obs)
    }
}
