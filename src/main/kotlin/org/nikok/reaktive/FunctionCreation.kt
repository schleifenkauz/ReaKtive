package org.nikok.reaktive

import org.nikok.reaktive.value.ReactiveBoolean
import org.nikok.reaktive.value.ReactiveValue
import kotlin.reflect.KFunction1

/**
 * @author Nikolaus Knop
 */

private abstract class ToStringDescribed : Described {
    override fun toString(): String = description
}

/**
 * @return a [Mapper] described by [name] which maps values of type [T] using the [map]
 */
fun <T, F> mapper(name: String, map: (T) -> F): Mapper<T, F> = map.asMapper(name)

/**
 * @return a [Mapper] using the name of the reflected function
 */
fun <T, F> (KFunction1<T, F>).asMapper() = mapper(name, this)

/**
 * @return a [Mapper] described by [name] which maps values of type [T] using the receiver function
 */
infix fun <T, F> ((T) -> F).asMapper(name: String): Mapper<T, F> {
    return object : Mapper<T, F>, ToStringDescribed() {
        override fun map(value: T): F {
            return invoke(value)
        }

        override val description = name
    }
}

/**
 * @return a [Predicate] described by [name] which tests values of type [T] using [test]
 */
fun <T> predicate(name: String, test: (T) -> Boolean): Predicate<T> = test.asPredicate(name)

/**
 * @return a [Predicate] using the name of the reflected function
 */
fun <T> (KFunction1<T, Boolean>).asPredicate() = predicate(name, this)

/**
 * @return a [Predicate] described by [name] which tests values of type [T] using the receiver function
 */
infix fun <T> ((T) -> Boolean).asPredicate(name: String): Predicate<T> {
    return object : Predicate<T>, ToStringDescribed() {
        override fun test(test: T): Boolean {
            return invoke(test)
        }

        override val description: String = name
    }
}

/**
 * @return a [ReactiveMapper] described by [name] which maps values of type [T] using [map]
 */
fun <T, F> reactiveMapper(name: String, map: (T) -> ReactiveValue<F>) = map.asReactiveMapper(name)

/**
 * @return a [ReactiveMapper] using the name of the reflected function
 * @throws IllegalArgumentException if the receiver is an anonymous function
 */
fun <T, F> (KFunction1<T, ReactiveValue<F>>).asReactiveMapper(): ReactiveMapper<T, F> {
    return reactiveMapper(name, this)
}

/**
 * @return a [ReactiveMapper] described by [name] which maps values of type [T] using the receiver function
 */
infix fun <T, F> ((T) -> ReactiveValue<F>).asReactiveMapper(name: String): ReactiveMapper<T, F> {
    return object : ReactiveMapper<T, F>, ToStringDescribed() {
        override fun map(value: T): ReactiveValue<F> {
            return invoke(value)
        }

        override val description = name
    }
}

/**
 * @return a [ReactivePredicate] described by [name] which tests values of type [T] using [test]
 */
fun <T> reactivePredicate(name: String, test: (T) -> ReactiveBoolean) = test.asReactivePredicate(name)

/**
 * @return a [ReactivePredicate] using the name of the reflected function
 */
fun <T> (KFunction1<T, ReactiveBoolean>).asReactivePredicate() = reactivePredicate(name, this)

/**
 * @return a [ReactivePredicate] described by [name] which tests values of type [T] using the receiver function
 */
infix fun <T> ((T) -> ReactiveBoolean).asReactivePredicate(name: String): ReactivePredicate<T> {
    return object : ReactivePredicate<T>, ToStringDescribed() {
        override fun test(t: T): ReactiveBoolean {
            return invoke(t)
        }

        override val description: String = name
    }
}

/**
 * @return an [InvalidationHandler] described by [desc] which uses [handle] to handle invalidation
 */
fun <T> invalidationHandler(desc: String, handle: (Reactive) -> T): InvalidationHandler = handle.withDescription(desc)

/**
 * @return a [InvalidationHandler] using the name of the reflected function
 * @throws IllegalArgumentException if the receiver is an anonymous function
 */
fun <T> (KFunction1<Reactive, T>).asInvalidationHandler(): InvalidationHandler {
    return invalidationHandler(name, this)
}

/**
 * @return an [InvalidationHandler] described by [desc] which uses the receiver function to handle invalidation
 */
infix fun <T> ((Reactive) -> T).withDescription(desc: String): InvalidationHandler {
    return object : InvalidationHandler, ToStringDescribed() {
        override fun invalidated(invalidated: Reactive) {
            invoke(invalidated)
        }

        override val description: String = desc
    }
}
