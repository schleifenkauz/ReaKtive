/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.list.binding

import org.nikok.reaktive.collection.binding.CollectionBinding
import org.nikok.reaktive.list.ListChange
import org.nikok.reaktive.list.ReactiveList

interface ListBinding<E>: CollectionBinding<E, ListChange<E>>, ReactiveList<E>