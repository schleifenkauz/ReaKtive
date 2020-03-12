/**
 *@author Nikolaus Knop
 */

package reaktive.map

sealed class MapChange<K, out V> {
    abstract val modified: ReactiveMap<K, V>

    data class Removed<K, out V>(
        override val modified: ReactiveMap<K, V>,
        val key: K,
        val value: V
    ) : MapChange<K, V>()

    data class Put<K, out V>(
        override val modified: ReactiveMap<K, V>,
        val key: K,
        val oldValue: V?,
        val newValue: V
    ) : MapChange<K, V>()
}