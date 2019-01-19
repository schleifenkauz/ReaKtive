/**
 * @author Nikolaus Knop
 */

package reaktive.set.binding

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.describedAs
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.dsl.*

internal fun <E> SpecBody.testSetBinding(
    binding: SetBinding<E>,
    expected: () -> Set<E>,
    block: SetBindingTestBody<E>.() -> Unit
) {
    val body = SetBindingTestBody(this, binding, expected)
    with(body) {
        "initially" {}
        block()
    }
}

internal class SetBindingTestBody<E>(
    private val spec: SpecBody,
    private val tested: SetBinding<E>,
    private val expectedSet: () -> Set<E>
) {
    inline operator fun String.invoke(crossinline action: () -> Unit) {
        spec.on(this) {
            action()
            val expected = expectedSet()
            it("should update the set to $expected") {
                tested.now describedAs "the binding" shouldMatch equalTo(expected)
            }
        }
    }
}