/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.collection.binding

import org.nikok.reaktive.Disposable
import org.nikok.reaktive.collection.CollectionChange
import org.nikok.reaktive.collection.ReactiveCollection

/**
 * A [Collection] binding
*/
interface CollectionBinding<out E, out Ch: CollectionChange<E>>: Disposable, ReactiveCollection<E, Ch>