/**
 * @author Nikolaus Knop
 */

package reaktive

import reaktive.collection.ReactiveCollection

/**
 * @return dependencies that are never invalidated
 */
fun noDependencies(): Dependencies = Dependencies.none()

/**
 * @return dependencies that are invalidated when on of the [reactives] is invalidated
 */
fun dependencies(vararg reactives: Reactive): Dependencies = dependencies(reactives.asList())

/**
 * @return dependencies that are invalidated when on of the [reactives] is invalidated
 */
fun dependencies(reactives: Collection<Reactive>): Dependencies = Dependencies.constant(reactives)

/**
 * Dependencies that cause a binding to recompute
 */
sealed class Dependencies : Reactive {
    private object None : Dependencies() {
        override fun observe(handler: InvalidationHandler) = Observer.nothing
    }

    private class Constant(private val dependencies: Collection<Reactive>) : Dependencies() {
        override fun observe(handler: InvalidationHandler): Observer {
            val observers = dependencies.map { it.observe(handler) }
            return Observer { observers.forEach(Observer::kill) }
        }
    }

    private class Dynamic(private val dependencies: ReactiveCollection<Reactive>) : Dependencies() {
        override fun observe(handler: InvalidationHandler): Observer {
            TODO()
        }
    }

    companion object {
        /**
         * @return [Dependencies] which will never be invalidated
         */
        fun none(): Dependencies = None

        /**
         * @return [Dependencies] which will be invalidated if one of the [reactives] is invalidated
         */
        fun constant(reactives: Collection<Reactive>): Dependencies = Constant(reactives)

        /**
         * @return [Dependencies] which will be invalidated if one of the [reactives] is invalidated
         */
        fun constant(vararg reactives: Reactive): Dependencies = constant(reactives.asList())

        /**
         * @return [Dependencies] which will be invalidated if one of the [reactives] is invalidated
         */
        fun dynamic(reactives: ReactiveCollection<Reactive>): Dependencies = Dynamic(reactives)
    }
}