/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.collection.binding

import org.nikok.reaktive.BindingBody
import org.nikok.reaktive.Observer
import org.nikok.reaktive.collection.CollectionWriter

/**
 * Used to setup a [CollectionBinding]
 */
interface CollectionBindingBody<E> : CollectionWriter<E>, BindingBody