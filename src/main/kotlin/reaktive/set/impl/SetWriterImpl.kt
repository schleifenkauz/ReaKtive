/**
 *@author Nikolaus Knop
 */

package reaktive.set.impl

import reaktive.collection.base.AbstractCollectionWriter
import reaktive.impl.HandlerCounter
import reaktive.set.MutableReactiveSet
import reaktive.set.SetWriter

internal class SetWriterImpl<E>(target: MutableReactiveSet<E>, handlerCounter: HandlerCounter)
    : SetWriter<E>, AbstractCollectionWriter<E>(target, handlerCounter)