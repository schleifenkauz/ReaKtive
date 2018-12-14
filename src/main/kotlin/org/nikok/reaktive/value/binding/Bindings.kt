package org.nikok.reaktive.value.binding

import org.nikok.reaktive.*
import org.nikok.reaktive.value.*

internal object Bindings {
    fun <T, F> map(rv: ReactiveValue<T>, newDescription: String, f: (T) -> F) =
            binding<F>(newDescription, dependencies(rv)) { f(rv.get()) }

    fun <T, F> flatMap(rv: ReactiveValue<T>, newDescription: String, f: (T) -> ReactiveValue<F>): Binding<F> {
        val first = f(rv.now)
        return binding(newDescription, first.now) {
            var oldBindObserver: Observer? = bind(first)
            val obs = rv.observe("observer for binding $newDescription") { _, _, new ->
                oldBindObserver?.kill()
                oldBindObserver = bind(f(new))
            }
            addObserver(obs)
        }
    }
}