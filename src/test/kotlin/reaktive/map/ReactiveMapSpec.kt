package reaktive.map

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given

object ReactiveMapSpec: Spek({
    given("a reactive map") {
        val m = reactiveMap<Int, Int>()
        m
    }
})