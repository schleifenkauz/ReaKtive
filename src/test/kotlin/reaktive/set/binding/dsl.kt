/**
 * @author Nikolaus Knop
 */

package reaktive.set.binding

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.dsl.*
import reaktive.binding.AbstractBindingsTestBody

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
    spec: SpecBody,
    private val tested: SetBinding<E>,
    private val expectedSet: () -> Set<E>
) : AbstractBindingsTestBody(spec) {
    override fun ActionBody.check() {
        val expected = expectedSet()
        it("should update the set to $expected") {
            tested.now shouldMatch equalTo(expected)
        }
    }
}