package reaktive.value.binding

import reaktive.Observer
import reaktive.dependencies
import reaktive.value.ReactiveValue
import reaktive.value.now

internal object Bindings {
    fun <T, F> map(rv: ReactiveValue<T>, f: (T) -> F) =
        binding<F>(dependencies(rv)) { f(rv.get()) }

    fun <T, F> flatMap(rv: ReactiveValue<T>, f: (T) -> ReactiveValue<F>): Binding<F> {
        val first = f(rv.now)
        return binding(first.now) {
            var oldBindObserver: Observer? = bind(first)
            val obs = rv.observe { _, _, new ->
                oldBindObserver?.kill()
                oldBindObserver = bind(f(new))
            }
            addObserver(obs)
        }
    }
}