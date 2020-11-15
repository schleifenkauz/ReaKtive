package reaktive.value.binding.impl

import reaktive.Observer
import reaktive.Reactive
import reaktive.collection.ReactiveCollection
import reaktive.list.ReactiveList
import reaktive.set.ReactiveSet
import reaktive.value.*
import reaktive.value.binding.AutoDependenciesBindingBody
import reaktive.value.binding.Binding

internal class AutoDependenciesBinding<T> private constructor(
    private val wrapped: ReactiveVariable<T>,
    private val body: BodyImpl,
    private val compute: AutoDependenciesBindingBody.() -> T
) : Binding<T>, ReactiveValue<T> by wrapped {
    private constructor(body: BodyImpl, compute: AutoDependenciesBindingBody.() -> T)
            : this(reactiveVariable(body.compute()), body, compute)

    constructor(compute: AutoDependenciesBindingBody.() -> T) : this(BodyImpl(), compute)

    private val observers = mutableMapOf<Reactive, Observer>()

    private fun observeDependencies() {
        val itr = observers.entries.iterator()
        for ((dep, obs) in itr) {
            if (dep !in body.dependencies) {
                obs.kill()
                itr.remove()
            }
        }
        for (dep in body.dependencies) {
            if (dep in observers) continue
            observers[dep] = dep.observe {
                wrapped.set(body.compute())
                observeDependencies()
            }
        }
        body.dependencies.clear()
    }

    init {
        observeDependencies()
    }

    private class BodyImpl : AutoDependenciesBindingBody {
        val dependencies = mutableSetOf<Reactive>()

        override fun dependOn(reactive: Reactive) {
            dependencies.add(reactive)
        }

        override fun <T> ReactiveValue<T>.invoke(): T = now.also { dependOn(this) }

        override fun <E> ReactiveCollection<E>.invoke(): Collection<E> = now.also { dependOn(this) }

        override fun <E> ReactiveList<E>.invoke(): List<E> = now.also { dependOn(this) }

        override fun <E> ReactiveSet<E>.invoke(): Set<E> = now.also { dependOn(this) }
    }

    override fun dispose() {
        for (obs in observers.values) obs.kill()
    }
}