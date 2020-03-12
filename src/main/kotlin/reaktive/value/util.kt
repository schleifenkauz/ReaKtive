/**
 * @author Nikolaus Knop
 */

package reaktive.value

import reaktive.Observer

fun <A, B> observe(
    v1: ReactiveValue<A>,
    v2: ReactiveValue<B>,
    handler1: (old: A, new: A, other: B) -> Unit,
    handler2: (old: B, new: B, other: A) -> Unit
): Observer = v1.observe { _, old, new -> handler1(old, new, v2.now) } and
        v2.observe { _, old, new -> handler2(old, new, v1.now) }