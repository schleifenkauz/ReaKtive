package reaktive.set

import com.natpryce.hamkrest.equalTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import reaktive.util.*
import java.util.*

object ReactiveSetSpec : Spek({
    Feature("a reactive set") {
        Scenario("mutating") {
            val s = reactiveSet(0, 1, 2, 3)
            val test = mutableSetOf(0, 1, 2, 3)
            testSameEffects(test, s.now) {
                "adding a new element" { add(4) }
                "adding an element already contained" { add(3) }
                "adding a few other elements" { addAll(listOf(1, 4, 5, 9, 10)) }
                "adding elements that already are in the set" { addAll(listOf(4, 1)) }
                "adding no elements" { addAll(emptyList()) }
                "removing an element" { remove(1) }
                "removing an element not contained" { remove(-4) }
                "removing a few other elements" { removeAll(listOf(-4, -1, 4, 10, 11)) }
                "removing no elements" { removeAll(emptyList()) }
                "remoeing elements that aren't in the set" { removeAll(listOf(-100)) }
                "clearing" { clear() }
            }
        }
        Scenario("iterating through an empty set") {
            val empty = reactiveSet<Unit>()
            When("hasNext") {
            }
            Then("it should return false") {
                empty.now.iterator().hasNext() shouldBe `false`
            }
            When("next") {
            }
            Then("it should throw NoSuchElementException") {
                { empty.now.iterator().next() } shouldBe throwing<NoSuchElementException>()
            }
        }
        Scenario("iterating through a set of one element") {
            val set = reactiveSet(1, 2, 3)
            val itr = set.now.iterator()
            When("hasNext") {
            }
            Then("it should return true") {
                itr.hasNext() shouldBe `true`
            }
            var el: Int = -1
            When("next") {
                el = itr.next()
            }
            Then("it should return the first element") {
                el shouldBe equalTo(1)
            }
            When("remove") {
                itr.remove()
            }
            Then("it should remove the first element") {
                set.now shouldBe equalTo(setOf(2, 3))
            }
        }
        Scenario("concurrent modification") {
            val set = reactiveSet(1, 2, 3)
            val itr = set.now.iterator()
            When("modifying the set after the iterator has been created and then calling next") {
                set.now.add(4)
            }
            Then("it should throw ConcurrentModificationException") {
                { itr.next(); Unit } shouldBe throwing<ConcurrentModificationException>()
            }
        }
    }
})