/**
 * @author Nikolaus Knop
 */

package reaktive.random

import java.util.concurrent.ThreadLocalRandom

interface Gen<out T> {
    fun next(): T

    companion object {
        operator fun <T> invoke(generator: () -> T): Gen<T> = object : Gen<T> {
            override fun next(): T = generator()
        }

        fun <T> constant(value: T) = Gen { value }

        fun int(min: Int = 0, bound: Int = Int.MAX_VALUE) =
            Gen { ThreadLocalRandom.current().nextInt(min, bound) }

        fun double(min: Double = 0.0, bound: Double = Double.MAX_VALUE) =
            Gen { ThreadLocalRandom.current().nextDouble(min, bound) }

        fun index(list: List<*>) =
            if (list.isNotEmpty()) Gen { ThreadLocalRandom.current().nextInt(list.size) }
            else throw IllegalArgumentException("List is empty")

        fun <E> fromList(list: List<E>) = Gen {
            val idx = index(list).next()
            list[idx]
        }

        val char = Gen { ThreadLocalRandom.current().nextInt(256).toChar() }

        fun string(length: Gen<Int>) = Gen { String(CharArray(length.next()) { char.next() }) }

        fun string(length: Int) = string(constant(length))

        fun <T> list(length: Gen<Int>, elements: Gen<T>) = Gen { List(length.next()) { elements.next() } }

        fun <T> list(length: Int, elements: Gen<T>) = list(constant(length), elements)

        fun <T> set(size: Gen<Int>, elements: Gen<T>) = Gen {
            mutableSetOf<T>().apply {
                repeat(size.next()) { add(elements.next()) }
            }
        }

        fun <T> set(size: Int, elements: Gen<T>) = set(constant(size), elements)

        fun <T> choose(vararg choices: T) = fromList(choices.asList())
    }
}

