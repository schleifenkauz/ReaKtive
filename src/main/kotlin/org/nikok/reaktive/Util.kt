/**
 * @author Nikolaus Knop
 */

package org.nikok.reaktive

import java.lang.ref.Reference
import kotlin.reflect.KProperty

internal operator fun <T : Any?> Reference<T>.getValue(receiver: Any?, property: KProperty<*>) = get()