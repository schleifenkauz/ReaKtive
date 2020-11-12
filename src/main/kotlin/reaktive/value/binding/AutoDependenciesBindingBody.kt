package reaktive.value.binding

import reaktive.Reactive
import reaktive.value.ReactiveValue

/**
 * Receiver used for bindings whose dependencies are automatically determined.
 */
interface AutoDependenciesBindingBody {
    /**
     * Ensures that the binding is reevaluated when the given [reactive] is invalidated.
     *
     * Note that the function that is used to reevaluate the binding does not register a dependency on
     * the given [reactive] again its repeated invalidation will not cause the binding to be reevaluated.
     */
    fun dependOn(reactive: Reactive)

    /**
     * Add a dependency on the given [ReactiveValue] return its current value.
     * @see dependOn
     */
    operator fun <T> ReactiveValue<T>.invoke(): T
}