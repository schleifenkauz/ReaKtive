/**
 * @author Nikolaus Knop
 */

package reaktive.list.fx

import javafx.collections.ObservableList
import reaktive.list.ReactiveList

/**
 * @return an [ObservableList] that delegates all calls to their counterparts in this [ReactiveList]
 */
fun <E> ReactiveList<E>.asObservableList(): ObservableList<E> = ObservableListAdapter(this)