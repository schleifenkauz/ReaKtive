package reaktive.value.binding

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import reaktive.dependencies
import reaktive.value.*

internal object BindingCreateSpec : Spek({
    given("a simple binding") {
        val dependency = reactiveVariable(34)
        fun compute() = dependency.now * 2
        val b = binding(dependencies(dependency), ::compute)
        testBinding(b, ::compute) {
            "set source to 2" {
                dependency.set(2)
            }
            "set source to -4" {
                dependency.set(-4)
            }
        }
    }
    given("an initialized complex binding") {
        val i1 = reactiveVariable(1)
        val i2 = reactiveVariable(2)
        val i3 = reactiveVariable(3)
        val b = binding(i1.now + i2.now + i3.now) {
            fun observe(dependency: ReactiveValue<Int>) {
                val obs =
                    dependency.observe { _, old, new ->
                            val diff = new - old
                            withValue { v ->
                                set(v + diff)
                            }
                        }
                addObserver(obs)
            }
            listOf(i1, i2, i3).forEach(::observe)
        }

        fun expected() = i1.now + i2.now + i3.now
        testBinding(b, ::expected) {
            "set first to 3" {
                i1.set(3)
            }
            "set second to -2" {
                i2.set(-2)
            }
            "set third to 30" {
                i3.set(30)
            }
        }
    }
})