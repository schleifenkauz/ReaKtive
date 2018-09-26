/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive.set.binding

import org.nikok.reaktive.collection.binding.CollectionBindingBody
import org.nikok.reaktive.set.SetWriter

interface SetBindingBody<E>: SetWriter<E>, CollectionBindingBody<E>