package reaktive.value

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.should.shouldMatch
import com.natpryce.hamkrest.should.shouldNotMatch
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.nikok.kref.weak
import reaktive.help.`true`
import reaktive.help.shouldBe
import reaktive.value.help.value

internal object ReactiveValueSetterSpec : Spek({
    var rv = reactiveVariable(23)
    val weak by weak(rv)
    given("the setter of a reactive variable") {
        val s = rv.setter
        on("setting to 34") {
            s.set(34)
            it("should set the reactive variable to 34") {
                rv shouldMatch value(equalTo(34))
            }
        }
        val bindTo = reactiveValue(45)
        on("binding it to a reactive value") {
            val obs = s.bind(bindTo)
            it("should bind to the reactive value") {
                rv.isBound shouldBe `true`
                rv shouldMatch value(equalTo(45))
                obs.kill()
            }
        }
        val obs = rv.observe { _ -> }
        test("when the reactive variable is observed the setter should hold a strong reference to it") {
            rv = reactiveVariable(-1) //some new reactive variable
            System.gc()
            weak shouldNotMatch absent()
        }
        on("removing the last handler") {
            obs.kill()
            test("the setter should only hold a weak reference") {
                System.gc()
                weak shouldBe absent()
            }
        }
    }
})