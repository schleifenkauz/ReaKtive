/**
 * @author Nikolaus Knop
 */

package reaktive.map

import reaktive.Observer
import reaktive.Reactive
import reaktive.collection.ReactiveCollection
import reaktive.set.ReactiveSet
import reaktive.value.ReactiveValue

interface ReactiveMap<K, out V> : Reactive {
    val now: Map<K, V>
    val keys: ReactiveSet<K>
    val values: ReactiveCollection<V>
    fun observeMap(handler: (MapChange<K, V>) -> Unit): Observer

    interface Entry<out K, out V> {
        val key: K
        val value: ReactiveValue<V>
    }
}