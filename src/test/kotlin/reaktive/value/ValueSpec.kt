package reaktive.value

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.include

internal object ValueSpec : Spek({
    given("a constant value 1") {
        val v = value(1)
        include(ValueSpec.common(v))
    }
}) {
    fun common(v: Value<Int>): Spek {
        return Spek.wrap {

        }
    }
}