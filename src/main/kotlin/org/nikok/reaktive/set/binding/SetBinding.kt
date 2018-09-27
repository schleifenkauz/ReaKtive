/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.set.binding

import org.nikok.reaktive.collection.binding.CollectionBinding
import org.nikok.reaktive.set.ReactiveSet
import org.nikok.reaktive.set.SetChange

interface SetBinding<out E>: CollectionBinding<E, SetChange<E>>, ReactiveSet<E>