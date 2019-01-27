/**
 * @author Nikolaus Knop
 */

package reaktive.list

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.dsl.*
import reaktive.binding.AbstractBindingsTestBody
import reaktive.list.binding.ListBinding

internal fun <E> SpecBody.testListBinding(
    binding: ListBinding<E>,
    expected: () -> List<E>,
    block: ListBindingTestBody<E>.() -> Unit
) {
    val body = ListBindingTestBody(this, binding, expected)
    with(body) {
        "initially" {}
        block()
    }
}

internal class ListBindingTestBody<E>(
    spec: SpecBody,
    private val tested: ListBinding<E>,
    private val expectedList: () -> List<E>
) : AbstractBindingsTestBody(spec) {
    override fun ActionBody.check() {
        val expected = expectedList()
        it("should update the list to $expected") {
            tested.now shouldMatch equalTo(expected)
        }
    }
}