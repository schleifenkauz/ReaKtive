package reaktive.list.fx

import javafx.collections.ObservableListBase
import reaktive.list.ListChange.*
import reaktive.list.ReactiveList

internal class ObservableListAdapter<E>(private val wrapped: ReactiveList<E>) : ObservableListBase<E>() {
    private val obs = wrapped.observeList { c ->
        beginChange()
        when (c) {
            is Added    -> nextAdd(c.index, c.index + 1)
            is Removed  -> nextRemove(c.index, c.element)
            is Replaced -> nextReplace(c.index, c.index + 1, mutableListOf(c.old))
        }
        endChange()
    }

    override fun get(index: Int): E = wrapped.now[index]

    override val size: Int
        get() = wrapped.now.size
}
