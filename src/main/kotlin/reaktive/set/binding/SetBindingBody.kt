/**
 * @author Nikolaus Knop
 */

package reaktive.set.binding

import reaktive.collection.binding.CollectionBindingBody
import reaktive.set.SetWriter

interface SetBindingBody<E>: SetWriter<E>, CollectionBindingBody<E>