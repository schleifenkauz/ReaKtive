package reaktive.set

import com.natpryce.hamkrest.should.describedAs
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import reaktive.random.Gen
import reaktive.set.binding.*
import reaktive.value.now
import reaktive.value.reactiveVariable

internal object BindingsSpec : Spek({
    describe("map binding") {
        val set = reactiveSet(1, 2, 3)
        val powers = set.map { it * it }
        fun expected() = set.now.mapTo(mutableSetOf()) { it * it }
        testSetBinding(powers, ::expected) {
            with(set.now) {
                "add an element" {
                    add(4)
                }
                "remove an element" {
                    remove(2)
                }
                "clear" {
                    clear()
                }
            }
        }
    }
    describe("flatMap binding") {
        val part1 = reactiveSet(1, 2, 3)
        val part2 = reactiveSet(3, 4, 5)
        val part3 = reactiveSet(5, 0, 5)
        val set = reactiveSet(part1, part2, part3)
        val flat = set.flatten()
        fun expected() = set.now.flatMapTo(mutableSetOf()) { it.now }
        testSetBinding(flat, ::expected) {
            val part4 = reactiveSet(6, 2, 7)
            "add new part" {
                set.now.add(part4)
            }
            "then add element to it" {
                part4.now.add(8)
            }
            "remove element from it" {
                part4.now.remove(6)
            }
            "again remove it" {
                set.now.remove(part4)
            }
            "remove another part" {
                set.now.remove(part2)
            }
            "remove element from initial part" {
                part1.now.remove(1)
            }
            "add element to initial part" {
                part2.now.add(9)
            }
        }
    }
    describe("union binding") {
        val set1 = reactiveSet(1, 2, 3)
        val set2 = reactiveSet(3, 4, 5)
        val union = set1 + set2
        fun expected() = set1.now + set2.now
        testSetBinding(union, ::expected) {
            "add new element" {
                set1.now.add(0)
            }
            "add element that was already there" {
                set2.now.add(1)
            }
            "remove common element" {
                set2.now.remove(3)
            }
            "remove unique element" {
                set1.now.remove(3)
            }
        }
    }
    describe("difference binding") {
        val set1 = reactiveSet(1, 2, 3)
        val set2 = reactiveSet(2, 3, 4)
        val difference = set1 - set2
        fun expected() = set1.now - set2.now
        testSetBinding(difference, ::expected) {
            with(set1.now) {
                "add element to source not existing in subtracted set" {
                    add(0)
                }
                "add element to source already existing in subtracted set" {
                    add(4)
                }
                "remove element from source not existing in subtracted set" {
                    remove(1)
                }
                "remove element from source also existing in subtracted set" {
                    remove(2)
                }
            }
            //now: set1 = { 0, 3, 4 }
            with(set2.now) {
                "add element to subtracted set also existing in source" {
                    add(0)
                }
                "add element to subtracted set not existing in source" {
                    add(5)
                }
                "remove element from subtracted set also existing in source" {
                    remove(3)
                }
                "remove element from subtracted set not existing in source" {
                    remove(5)
                }
            }
            repeat(5) {
                mutateRandomly(set1.now describedAs "the source set", Gen.int(0, 1000))
            }
            repeat(5) {
                mutateRandomly(set2.now describedAs "the subtracted set", Gen.int(0, 1000))
            }
        }
    }
    describe("intersection binding") {
        val set1 = reactiveSet(1, 2, 3)
        val set2 = reactiveSet(3, 4, 5)
        val intersection = set1.intersect(set2)
        fun expected() = set1.now.intersect(set2.now)
        testSetBinding(intersection, ::expected) {
            "add new element" {
                set1.now.add(0)
            }
            "add element that was already there" {
                set2.now.add(1)
            }
            "remove common element" {
                set2.now.remove(3)
            }
            "remove unique element" {
                set1.now.remove(3)
            }
            repeat(5) {
                mutateRandomly(set1.now describedAs "the first set", Gen.int(0, 1000))
            }
            repeat(5) {
                mutateRandomly(set2.now describedAs "the second set", Gen.int(0, 1000))
            }
        }
    }
    describe("values binding") {
        val v1 = reactiveVariable(1)
        val v2 = reactiveVariable(2)
        val v3 = reactiveVariable(3)
        val source = reactiveSet(v1, v2, v3)
        val values = source.values()
        fun expected() = source.now.mapTo(mutableSetOf()) { it.now }
        testSetBinding(values, ::expected) {
            "modify a variable" {
                v1.set(4)
            }
            "remove a variable" {
                source.now.remove(v2)
            }
            "then modify the removed variable" {
                v2.set(5)
            }
            "add it again" {
                source.now.add(v2)
            }
            "modify it again" {
                v2.set(7)
            }
            val v4 = reactiveVariable(10)
            "add a completely new variable" {
                source.now.add(v4)
            }
            "then modify the new variable" {
                v4.set(11)
            }
            "remove it again" {
                source.now.remove(v4)
            }
            "and then modify it" {
                v4.set(12)
            }
            val toModify = Gen.choose(v1, v2, v3, v4)
            val value = Gen.int(-1000, 1000)
            repeat(5) {
                "modify variable randomly" {
                    toModify.next().set(value.next())
                }
            }
        }
    }
})