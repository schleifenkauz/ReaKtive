package org.nikok.reaktive

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.should.shouldMatch
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.nikok.reaktive.mocks.TestKill
import java.lang.ref.WeakReference

/**
 *@author Nikolaus Knop
 */

internal object ObserverSpec : Spek({
    given("an observer killing a TestKill") {
        on("invoking kill") {
            val observer = Observer("test", TestKill)
            observer.kill()
            it("should kill the mock") {
                TestKill.killed shouldMatch equalTo(true)
            }
        }
        on("finalizing it") {
            val observer by WeakReference(Observer("test", TestKill))
            System.gc()
            Thread.sleep(10)
            observer shouldMatch absent()
            it("should kill the mock") {
                TestKill.killed shouldMatch equalTo(true)
            }
        }
        on("killing it after it was already killed") {
            val observer = Observer("test", TestKill)
            observer.kill()
            it("should throw an IllegalStateException") {
                { observer.kill() } shouldMatch throws<IllegalStateException>()
            }
        }
        on("trying to kill it after it was already killed") {
            val observer = Observer("test", TestKill)
            observer.kill()
            TestKill.reset()
            it("It should have no effect") {
                observer.tryKill()
                TestKill.killed shouldMatch equalTo(false)
            }
        }
        on("finalizing after it was already killed") {
            val observer by WeakReference(Observer("test", TestKill))
            observer?.kill()
            TestKill.reset()
            System.gc()
            it("should have no effect") {
                TestKill.killed shouldMatch equalTo(false)
            }
        }
    }
})
