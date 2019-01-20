/**
 * @author Nikolaus Knop
 */

package reaktive.collection.binding

import reaktive.BindingBody
import reaktive.collection.CollectionWriter

/**
 * Used to setup a [CollectionBinding]
 */
interface CollectionBindingBody<E> : CollectionWriter<E>, BindingBody