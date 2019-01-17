/**
 *@author Nikolaus Knop
 */

package reaktive.collection.binding

import reaktive.Disposable
import reaktive.collection.ReactiveCollection

/**
 * A [Collection] binding
*/
interface CollectionBinding<out E> : Disposable, ReactiveCollection<E>