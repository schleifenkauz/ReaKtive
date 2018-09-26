/**
 *@author Nikolaus Knop
 */

package org.nikok.reaktive.set.impl

import org.nikok.reaktive.collection.base.AbstractCollectionWriter
import org.nikok.reaktive.impl.HandlerCounter
import org.nikok.reaktive.set.MutableReactiveSet
import org.nikok.reaktive.set.SetWriter

internal class SetWriterImpl<E>(target: MutableReactiveSet<E>, handlerCounter: HandlerCounter)
    : SetWriter<E>, AbstractCollectionWriter<E>(target, handlerCounter)