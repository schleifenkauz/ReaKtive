package reaktive.list.binding.impl

import reaktive.collection.ReactiveCollection
import reaktive.list.ListChange.*
import reaktive.list.ReactiveList
import reaktive.list.binding.ListBinding
import reaktive.list.binding.listBinding
import reaktive.value.binding.Binding

internal object ListBindings {
    fun <E> concat(list: ReactiveList<E>, other: ReactiveCollection<E>): ListBinding<E> =
        listBinding(list.now + other.now) {
            var sizeOfFirst = list.now.size
            val o1 = list.observeList { c ->
                when (c) {
                    is Removed  -> {
                        sizeOfFirst--
                        removeAt(c.index)
                    }
                    is Added    -> {
                        sizeOfFirst++
                        add(c.index, c.element)
                    }
                    is Replaced -> {
                        set(c.index, c.element)
                    }
                }
            }
            val o2 = other.observeCollection { c ->
                if (c.wasAdded) add(c.element)
            }
        }

    fun <E> subtract(list: ReactiveList<E>, other: ReactiveCollection<E>): ListBinding<E> {
        TODO("not implemented")
    }

    fun <E, F> flatMap(list: ReactiveList<E>, f: (E) -> ReactiveCollection<F>): ListBinding<F> {
        TODO("not implemented")
    }

    fun <E, T> fold(list: ReactiveList<E>, initial: T, op: (T, E) -> T): Binding<T> {
        TODO("not implemented")
    }
}