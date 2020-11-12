package reaktive.value.binding

import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import reaktive.dependencies
import reaktive.util.shouldBe
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
        val b = createBinding(i1.now + i2.now + i3.now) {
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
    given("an auto dependencies binding") {
        val cond = reactiveVariable(false)
        val x = reactiveVariable(1)
        val y = reactiveVariable(2)
        var count = 0
        val b = binding {
            count++
            if (cond()) x() else x() + y()
        }

        fun expected() = if (cond.now) x.now else x.now + y.now
        on("creating it") {
            it("should compute the function") {
                count shouldBe equalTo(1)
                b.now shouldBe equalTo(3)
            }
        }
        on("setting on of the integers") {
            y.set(3)
            it("should recompute the sum") {
                count shouldBe equalTo(2)
                b.now shouldBe equalTo(4)
            }
        }
        on("setting the condition") {
            cond.set(true)
            it("should recompute the sum") {
                count shouldBe equalTo(3)
                b.now shouldBe equalTo(1)
            }
        }
        on("setting a variable that doesn't affect the result") {
            y.set(4)
            it("should not recompute") {
                count shouldBe equalTo(3)
            }
        }
        on("setting a variable that does affect the result") {
            x.set(0)
            it("should recompute") {
                count shouldBe equalTo(4)
                b.now shouldBe equalTo(0)
            }
        }
        on("flipping the condition") {
            cond.set(false)
            it("should recompute") {
                count shouldBe equalTo(5)
                b.now shouldBe equalTo(4)
            }
        }
        on("setting y") {
            y.set(5)
            it("should recompute") {
                count shouldBe equalTo(6)
                b.now shouldBe equalTo(5)
            }
        }
    }
})