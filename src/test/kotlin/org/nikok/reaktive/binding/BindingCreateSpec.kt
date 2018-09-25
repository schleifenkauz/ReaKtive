package org.nikok.reaktive.binding

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.nikok.reaktive.binding.help.testBinding
import org.nikok.reaktive.dependencies
import org.nikok.reaktive.value.*
import org.nikok.reaktive.value.binding.binding

internal object BindingCreateSpec : Spek({
    given("a simple binding") {
        val dependency = reactiveVariable("dep", 34)
        fun compute() = dependency.now * 2
        val b = binding("double", dependencies(dependency), ::compute)
        testBinding(b, expected = ::compute) {
            set(dependency) {
                toAll(1, -4, 5, 0, 0xFF)
            }
        }
    }
    given("an initialized complex binding") {
        val i1 = reactiveVariable("1", 1)
        val i2 = reactiveVariable("2", 2)
        val i3 = reactiveVariable("3", 3)
        val b = binding("sum of three ints", i1.now + i2.now + i3.now) {
            fun observe(dependency: ReactiveValue<Int>) {
                val obs =
                        dependency.observe("observer of $dependency") { _, old, new ->
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
            set(i1) {
                toAll(4, 2, -4, -7)
            }
            set(i2) {
                toAll(4, 3, 1, -0)
            }
            set(i3) {
                toAll(3, 1, 5, 0, -3)
            }
        }
    }
})