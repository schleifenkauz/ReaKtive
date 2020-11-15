package reaktive.value

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.equalTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.Observer
import reaktive.getValue
import reaktive.util.*
import reaktive.util.value
import java.lang.ref.WeakReference

internal object ReactiveValueSetterSpec : Spek({
    var rv = reactiveVariable(23)
    val weak by WeakReference(rv)
    Feature("the setter of a reactive variable") {
        val s = rv.setter
        Scenario("setting") {
            When("setting to 34") {
                s.set(34)
            }
            Then("should set the reactive variable to 34") {
                rv shouldMatch value(equalTo(34))
            }
        }
        val bindTo = reactiveValue(45)
        Scenario("binding") {
            lateinit var obs: Observer
            When("binding to a reactive value") {
                obs = s.bind(bindTo)
            }
            Then("should bind to the reactive value") {
                rv.isBound shouldBe `true`
                rv shouldMatch value(equalTo(45))
                obs.kill()
            }
        }
        Scenario("garbage collection") {
            lateinit var obs: Observer
            When("the reactive variable is observed") {
                obs = rv.observe { _ -> }
            }
            Then(" the setter should hold a strong reference to it") {
                rv = reactiveVariable(-1) //some new reactive variable
                System.gc()
                weak shouldMatch !absent<ReactiveInt>()
            }
            When("removing the last handler") {
                obs.kill()
            }
            Then("the setter should only hold a weak reference") {
                System.gc()
                weak shouldBe absent()
            }
        }
    }
})