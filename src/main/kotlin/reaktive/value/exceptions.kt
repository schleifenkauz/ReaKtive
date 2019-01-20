/**
 * @author Nikolaus Knop
 */

package reaktive.value

/**
 * An [IllegalStateException] which occurs when a [Variable] was tried to set or bind while being bound
 * @constructor
 * @param boundVar the [Variable] which was tried to set or bind while already being bound to another [ReactiveValue]
 * @param attemptedAction a description of the action which is forbidden in bound state
 */
class AlreadyBoundException(
    boundVar: Variable<*>, attemptedAction: String
) : IllegalStateException("Cannot $attemptedAction while $boundVar is bound") {
    internal companion object {
        fun attemptedSetTo(boundVar: Variable<*>, value: Any?): AlreadyBoundException {
            return AlreadyBoundException(boundVar, "set to $value")
        }

        fun attemptedBindTo(boundVar: Variable<*>, reactiveValue: ReactiveValue<*>): AlreadyBoundException {
            return AlreadyBoundException(boundVar, "bind to $reactiveValue")
        }
    }
}