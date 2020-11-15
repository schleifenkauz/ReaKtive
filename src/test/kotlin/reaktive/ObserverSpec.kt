package reaktive

import com.natpryce.hamkrest.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.mocks.TestKill
import reaktive.util.shouldMatch
import java.lang.ref.WeakReference

/**
 *@author Nikolaus Knop
 */

internal object ObserverSpec : Spek({
    Feature("observers") {
        Scenario("an observer killing a TestKill") {
            When("invoking kill") {
                val observer = Observer(TestKill)
                observer.kill()
            }
            Then("it should kill the mock") {
                TestKill.killed shouldMatch equalTo(true)
            }
            When("finalizing it") {
                val observer by WeakReference(Observer(TestKill))
                System.gc()
                Thread.sleep(10)
                observer shouldMatch absent()
            }
            Then("it should kill the mock") {
                TestKill.killed shouldMatch equalTo(true)
            }
            lateinit var observer: Observer
            When("killing it after it was already killed") {
                observer = Observer(TestKill)
                observer.kill()
            }
            Then("it should throw an IllegalStateException") {
                { observer.kill() } shouldMatch throws<IllegalStateException>()
            }
            When("trying to kill it after it was already killed") {
                observer = Observer(TestKill)
                observer.kill()
                TestKill.reset()
            }
            Then("it should have no effect") {
                observer.tryKill()
                TestKill.killed shouldMatch equalTo(false)
            }
            When("finalizing after it was already killed") {
                var obs: Observer? = Observer(TestKill)
                obs!!.kill()
                obs = null
                TestKill.reset()
                System.gc()
            }
            Then("it should have no effect") {
                TestKill.killed shouldMatch equalTo(false)
            }
        }
    }
})
